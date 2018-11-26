package ru.naumen.sd40.log.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parsers.ParsingUtils;
import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.TopTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.factories.TimeParserFactory;
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
    private boolean traceResult;
    private String timeZone;
    private String log;
    private String parseMode;

    private Map<String, IDataParser> dataParsers;
    private Map<String, DataSetFactory> dataSetFactories;
    private Map<String, TimeParserFactory> timeFactories;

    @Autowired
    public LogParser(Map<String, IDataParser> dataParsers,
                     Map<String, DataSetFactory> dataSetFactories,
                     Map<String, TimeParserFactory> timeFactories) {
        this.dataParsers = dataParsers;
        this.dataSetFactories = dataSetFactories;
        this.timeFactories = timeFactories;
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
        this.log = log;
        this.timeZone = timeZone;
        this.parseMode = parseMode;
        ITimeParser timeParser = buildTimeParser();
        IDataParser dataParser = buildDataParser();
        DataSetFactory dataSetFactory = getDataSetFactory();

        if (this.traceResult)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        try (InfluxDAOWorker influxDAOWorker = buildDaoWorker(dbName, influxDAO, dataSetFactory)) {
            parseEntries(this.log, timeParser, dataParser, influxDAOWorker);
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

    private ITimeParser buildTimeParser() {
        TimeParserFactory factory = timeFactories.get(this.parseMode + "TimeParserFactory");

        checkParseObject(factory);

        ITimeParser timeParser = factory.create();
        prepareTimeParser(timeParser);

        return timeParser;
    }

    // Можно вместо этого метода сделать Spring-аннотации After в фабриках TimeParser'ов
    private void prepareTimeParser(ITimeParser timeParser) {
        if (this.timeZone != null)
            timeParser.configureTimeZone(timeZone);

        if (timeParser instanceof TopTimeParser)
            ((TopTimeParser) timeParser).associateFile(this.log);
    }

    private IDataParser buildDataParser() {
        IDataParser dataParser = dataParsers.get(this.parseMode + "DataParser");

        checkParseObject(dataParser);

        return dataParser;
    }

    private DataSetFactory getDataSetFactory() {
        DataSetFactory factory = dataSetFactories.get(parseMode + "DataSetFactory");

        checkParseObject(factory);

        return factory;
    }

    private void checkParseObject(Object parseObject) {
        if (parseObject == null) {
            throw new IllegalArgumentException(
                    "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + this.parseMode);
        }
    }
}
