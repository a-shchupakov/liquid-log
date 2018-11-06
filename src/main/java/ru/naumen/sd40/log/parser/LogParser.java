package ru.naumen.sd40.log.parser;

import org.springframework.stereotype.Component;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parsers.ParsingUtils;
import ru.naumen.sd40.log.parser.parsers.data.GCDataParser;
import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.data.SdngDataParser;
import ru.naumen.sd40.log.parser.parsers.data.TopDataParser;
import ru.naumen.sd40.log.parser.parsers.time.GCTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.SdngTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.TopTimeParser;
import ru.naumen.sd40.log.parser.storages.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by doki on 22.10.16.
 */
@Component
public class LogParser
{
    private boolean traceResult;

    /**
     *
     * @throws IOException
     * @throws ParseException
     */
    public void parseLogs(String log, String parseMode, String dbName, String timeZone, boolean trace,
                          InfluxDAO influxDAO) throws IOException, ParseException
    {
        ITimeParser timeParser = buildTimeParser(log, parseMode);
        IDataParser dataParser = buildDataParser(parseMode);

        configureTimeZone(timeZone, timeParser);

        if (traceResult)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        try (InfluxDAOWorker influxDAOWorker = buildDaoWorker(dbName, influxDAO)) {
            parseEntries(log, timeParser, dataParser, influxDAOWorker);
        }
    }
    private InfluxDAOWorker buildDaoWorker(String dbName, InfluxDAO influxDAO) {
        InfluxDAOWorker influxDAOWorker = null;
        if (dbName != null) {
            influxDAOWorker = new InfluxDAOWorker(influxDAO, this.traceResult);
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

                DataSet dataSet = influxDAOWorker.getDataSet(key);
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
        switch (parseMode)
        {
            case "sdng":
                return new SdngDataParser();
            case "gc":
                return new GCDataParser();
            case "top":
                return new TopDataParser();
            default:
                throw new IllegalArgumentException(
                        "Unknown parse mode! Available modes: sdng, gc, top. Requested mode: " + parseMode);
        }
    }
}
