package ru.naumen.sd40.log.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
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

    private Map<String, ParseMode> parseModes;

    @Autowired
    public LogParser(Map<String, ParseMode> parseModes) {
        this.parseModes = parseModes;
    }

    /**
     *
     * @throws IOException
     * @throws ParseException
     */
    public void parseLogs(String log, String strParseMode, String dbName, String timeZone, boolean trace,
                          InfluxDAO influxDAO) throws IOException, ParseException
    {
        this.traceResult = trace;
        this.log = log;
        this.timeZone = timeZone;

        ParseMode parseMode = getParseMode(strParseMode);

        ITimeParser timeParser = parseMode.getTimeParser();
        IDataParser dataParser = parseMode.getDataParser();
        DataSetFactory dataSetFactory = parseMode.getDataSetFactory();

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

    private ParseMode getParseMode(String strParseMode) {
        ParseMode parseMode = parseModes.get(strParseMode);

        if (parseMode == null) {
            throw new IllegalArgumentException(
                    "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + strParseMode);
        }

        prepareTimeParser(parseMode.getTimeParser());

        return parseMode;
    }

    // Можно вместо этого метода сделать Spring-аннотации After в фабриках TimeParser'ов
    private void prepareTimeParser(ITimeParser timeParser) {
        if (this.timeZone != null)
            timeParser.configureTimeZone(timeZone);

        if (timeParser instanceof TopTimeParser)
            ((TopTimeParser) timeParser).associateFile(this.log);
    }
}
