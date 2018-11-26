package ru.naumen.sd40.log.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parsers.ParsingUtils;
import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.time.GCTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.SdngTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.TopTimeParser;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.factories.DataSetFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by doki on 22.10.16.
 */
@Component
public class LogParser
{
    private Map<String, IDataParser> dataParsers;
    private Map<String, DataSetFactory> dataSetFactories;
    private boolean traceResult;

    @Autowired
    public LogParser(Map<String, IDataParser> dataParsers, Map<String, DataSetFactory> dataSetFactories) {
        this.dataParsers = dataParsers;
        this.dataSetFactories = dataSetFactories;
    }

    /**
     *
     * @throws IOException
     * @throws ParseException
     */
    public void parseLogs(String log, String parseMode, String dbName, String timeZone, boolean trace,
                          InfluxDAO influxDAO) throws IOException, ParseException
    {
        this.traceResult = trace;
        ITimeParser timeParser = buildTimeParser(log, parseMode);
        IDataParser dataParser = buildDataParser(parseMode);
        DataSetFactory dataSetFactory = getDataSetFactory(parseMode);

        configureTimeZone(timeZone, timeParser);

        if (this.traceResult)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        try (InfluxDAOWorker influxDAOWorker = buildDaoWorker(dbName, influxDAO, dataSetFactory)) {
            parseEntries(log, timeParser, dataParser, influxDAOWorker);
        }
    }
    private InfluxDAOWorker buildDaoWorker(String dbName, InfluxDAO influxDAO, DataSetFactory dataSetFactory) {
        InfluxDAOWorker influxDAOWorker = null;
        if (dbName != null) {
            influxDAOWorker = new InfluxDAOWorker(influxDAO, traceResult, dataSetFactory);
            influxDAOWorker.init(dbName);
        }
        return influxDAOWorker;
    }

    private void configureTimeZone(String timeZone, ITimeParser parser) {
        if (timeZone != null)
        {
            parser.configureTimeZone(timeZone);
        }
    }

    private void parseEntries(String log, ITimeParser timeParser, IDataParser dataParser,
                                     InfluxDAOWorker influxDAOWorker) throws IOException, ParseException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(log)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                long time = timeParser.parseLine(line);
                if (time == 0)
                    continue;

                long key = ParsingUtils.roundToFiveMins(time);

                IDataSet dataSet = influxDAOWorker.getDataSet(key);
                dataParser.parseLine(line, dataSet);
            }
        }
    }

    private ITimeParser buildTimeParser(String log, String parseMode) {
        switch (parseMode)
        {
            case "sdng":
                return new SdngTimeParser();
            case "gc":
                return new GCTimeParser();
            case "top":
                return new TopTimeParser(log);
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + parseMode);
        }
    }

    private IDataParser buildDataParser(String parseMode) {
        IDataParser dataParser = dataParsers.get(parseMode + "DataParser");

        if (dataParser == null) {
            throw new IllegalArgumentException(
                    "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + parseMode);
        }

        return dataParser;
    }

    private DataSetFactory getDataSetFactory(String parseMode) {
        DataSetFactory factory = dataSetFactories.get(parseMode + "DataSetFactory");

        if (factory == null) {
            throw new IllegalArgumentException(
                    "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + parseMode);
        }

        return factory;
    }
}
