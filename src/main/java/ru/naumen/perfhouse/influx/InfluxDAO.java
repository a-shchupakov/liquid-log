package ru.naumen.perfhouse.influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.statdata.Constants;
import ru.naumen.sd40.log.parser.parsers.data.DataSet;
import ru.naumen.sd40.log.parser.storages.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.naumen.perfhouse.statdata.Constants.GarbageCollection.*;
import static ru.naumen.perfhouse.statdata.Constants.PerformedActions.*;
import static ru.naumen.perfhouse.statdata.Constants.ResponseTimes.*;
import static ru.naumen.perfhouse.statdata.Constants.Top.*;

/**
 * Created by doki on 24.10.16.
 */
@Component
public class InfluxDAO
{
    private String influxHost;

    private String user;

    private String password;

    private InfluxDB influx;

    @Autowired
    public InfluxDAO(@Value("${influx.host}") String influxHost, @Value("${influx.user}") String user,
            @Value("${influx.password}") String password)
    {
        this.influxHost = influxHost;
        this.user = user;
        this.password = password;
    }

    public void connectToDB(String dbName)
    {
        influx.createDatabase(dbName);
    }

    @PreDestroy
    public void destroy()
    {
        influx.disableBatch();
    }

    public QueryResult.Series executeQuery(String dbName, String query) {
        Query q = new Query(query, dbName);
        QueryResult result = influx.query(q);

        if (result.getResults().get(0).getSeries() == null)
        {
            return null;
        }

        return result.getResults().get(0).getSeries().get(0);
    }

    public List<String> getDbList()
    {
        return influx.describeDatabases();
    }

    @PostConstruct
    public void init()
    {
        influx = InfluxDBFactory.connect(influxHost, user, password);
    }

    public BatchPoints startBatchPoints(String dbName)
    {
        return BatchPoints.database(dbName).build();
    }

    private Builder addSdngFields(Builder builder, ActionDataStorage dones, ErrorDataStorage errors)
    {
        //@formatter:off
        return builder
                    .addField(COUNT, dones.getCount())
                    .addField("min", dones.getMin())
                    .addField(MEAN, dones.getMean())
                    .addField(STDDEV, dones.getStddev())
                    .addField(PERCENTILE50, dones.getPercent50())
                    .addField(PERCENTILE95, dones.getPercent95())
                    .addField(PERCENTILE99, dones.getPercent99())
                    .addField(PERCENTILE999, dones.getPercent999())
                    .addField(MAX, dones.getMax())
                    .addField(ERRORS, errors.getErrorCount())
                    .addField(ADD_ACTIONS, dones.getAddObjectActions())
                    .addField(EDIT_ACTIONS, dones.getEditObjectsActions())
                    .addField(LIST_ACTIONS, dones.getListActions())
                    .addField(COMMENT_ACTIONS, dones.getCommentActions())
                    .addField(GET_FORM_ACTIONS, dones.getFormActions())
                    .addField(GET_DT_OBJECT_ACTIONS, dones.getDtObjectActions())
                    .addField(SEARCH_ACTIONS, dones.getSearchActions())
                    .addField(GET_CATALOG_ACTIONS, dones.getCatalogsActions());

        //@formatter:on
    }

    public void storeFromJSon(BatchPoints batch, String dbName, JSONObject data) {
        influx.createDatabase(dbName);
        long timestamp = (data.getLong("time"));
        long errors = (data.getLong("errors"));
        double p99 = (data.getDouble("tnn"));
        double p999 = (data.getDouble("tnnn"));
        double p50 = (data.getDouble("tmed"));
        double p95 = (data.getDouble("tn"));
        long count = (data.getLong("tcount"));
        double mean = (data.getDouble("avg"));
        double stddev = (data.getDouble("dev"));
        long max = (data.getLong("tmax"));
        long herrors = data.getLong("hErrors");

        Point measure = Point.measurement("perf").time(timestamp, TimeUnit.MILLISECONDS).addField("count", count)
                .addField("min", 0).addField("mean", mean).addField("stddev", stddev).addField("percent50", p50)
                .addField("percent95", p95).addField("percent99", p99).addField("percent999", p999).addField("max", max)
                .addField("errors", errors).addField("herrors", herrors).build();

        if (batch != null)
        {
            batch.getPoints().add(measure);
        }
        else
        {
            influx.write(dbName, "autogen", measure);
        }
    }

    private Builder addGcFields(Builder builder, GcDataStorage dataStorage) {
        return builder
                    .addField(GCTIMES, dataStorage.getGcTimes())
                    .addField(AVARAGE_GC_TIME, dataStorage.getCalculatedAvg())
                    .addField(MAX_GC_TIME, dataStorage.getMaxGcTime());
    }

    private Builder addTopFields(Builder builder, TopDataStorage dataStorage) {
        return builder
                    .addField(AVG_LA, dataStorage.getAvgLa())
                    .addField(AVG_CPU, dataStorage.getAvgCpuUsage())
                    .addField(AVG_MEM, dataStorage.getAvgMemUsage())
                    .addField(MAX_LA, dataStorage.getMaxLa())
                    .addField(MAX_CPU, dataStorage.getMaxCpu())
                    .addField(MAX_MEM, dataStorage.getMaxMem());
    }

    public void storeData(BatchPoints batch, String dbName, long date, DataSet dataSet) {
        Builder builder = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS);

        ActionDataStorage actionDataStorage = dataSet.getActionsData();
        actionDataStorage.calculate();
        if (!actionDataStorage.isNaN())
            builder = addSdngFields(builder, actionDataStorage, dataSet.getErrorData());
        else if (!dataSet.getGcData().isNaN())
            builder = addGcFields(builder, dataSet.getGcData());
        else if (!dataSet.getCpuData().isNaN())
            builder = addTopFields(builder, dataSet.getCpuData());
        else
            return;

        Point point = builder.build();
        if (batch != null)
        {
            batch.getPoints().add(point);
        }
        else
        {
            influx.write(dbName, "autogen", point);
        }
    }

    public void writeBatch(BatchPoints batch)
    {
        influx.write(batch);
    }
}
