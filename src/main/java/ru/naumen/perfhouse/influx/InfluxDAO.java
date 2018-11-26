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
import ru.naumen.sd40.log.parser.storages.*;
import ru.naumen.sd40.log.parser.storages.dataSets.GcDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.SdngDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.TopDataSet;

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
public class InfluxDAO implements IDataBase
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

    @Override
    public void storeSdng(String dbName, long date, SdngDataSet dataSet, boolean traceResult) {
        Builder builder = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS);

        ActionDataStorage actionData = dataSet.getAction();
        ErrorDataStorage errorData = dataSet.getError();

        actionData.calculate();

        if (actionData.isNaN())
            return;

        if (traceResult) {
            System.out.print(String.format("%d;%d;%f;%f;%f;%f;%f;%f;%f;%f;%d\n", date, actionData.getCount(),
                    actionData.getMin(), actionData.getMean(), actionData.getStddev(), actionData.getPercent50(),
                    actionData.getPercent95(), actionData.getPercent99(), actionData.getPercent999(),
                    actionData.getMax(), errorData.getErrorCount()));
        }

        builder
            .addField(COUNT, actionData.getCount())
            .addField("min", actionData.getMin())
            .addField(MEAN, actionData.getMean())
            .addField(STDDEV, actionData.getStddev())
            .addField(PERCENTILE50, actionData.getPercent50())
            .addField(PERCENTILE95, actionData.getPercent95())
            .addField(PERCENTILE99, actionData.getPercent99())
            .addField(PERCENTILE999, actionData.getPercent999())
            .addField(MAX, actionData.getMax())
            .addField(ERRORS, errorData.getErrorCount())
            .addField(ADD_ACTIONS, actionData.getAddObjectActions())
            .addField(EDIT_ACTIONS, actionData.getEditObjectsActions())
            .addField(LIST_ACTIONS, actionData.getListActions())
            .addField(COMMENT_ACTIONS, actionData.getCommentActions())
            .addField(GET_FORM_ACTIONS, actionData.getFormActions())
            .addField(GET_DT_OBJECT_ACTIONS, actionData.getDtObjectActions())
            .addField(SEARCH_ACTIONS, actionData.getSearchActions())
            .addField(GET_CATALOG_ACTIONS, actionData.getCatalogsActions());

        Point point = builder.build();
        writePoint(dbName, point);
    }

    @Override
    public void storeGc(String dbName, long date, GcDataSet dataSet, boolean traceResult) {
        Builder builder = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS);

        GСDataStorage dataStorage = dataSet.get();

        if (dataStorage.isNaN())
            return;

        builder
            .addField(GCTIMES, dataStorage.getGcTimes())
            .addField(AVARAGE_GC_TIME, dataStorage.getCalculatedAvg())
            .addField(MAX_GC_TIME, dataStorage.getMaxGcTime());

        Point point = builder.build();
        writePoint(dbName, point);
    }

    @Override
    public void storeTop(String dbName, long date, TopDataSet dataSet, boolean traceResult) {
        Builder builder = Point.measurement(Constants.MEASUREMENT_NAME).time(date, TimeUnit.MILLISECONDS);

        TopDataStorage dataStorage = dataSet.get();

        if (dataStorage.isNaN())
            return;

        builder
            .addField(AVG_LA, dataStorage.getAvgLa())
            .addField(AVG_CPU, dataStorage.getAvgCpuUsage())
            .addField(AVG_MEM, dataStorage.getAvgMemUsage())
            .addField(MAX_LA, dataStorage.getMaxLa())
            .addField(MAX_CPU, dataStorage.getMaxCpu())
            .addField(MAX_MEM, dataStorage.getMaxMem());

        Point point = builder.build();
        writePoint(dbName, point);
    }


    private void writeBatch(BatchPoints batch)
    {
        influx.write(batch);
    }

    private void writePoint(String dbName, Point point) {
        influx.write(dbName, "autogen", point);
    }
}
