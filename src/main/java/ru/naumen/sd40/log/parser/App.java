package ru.naumen.sd40.log.parser;

import ru.naumen.sd40.log.parser.parsers.ParsingUtils;
import ru.naumen.sd40.log.parser.parsers.data.*;
import ru.naumen.sd40.log.parser.parsers.time.GCTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.SdngTimeParser;
import ru.naumen.sd40.log.parser.parsers.time.TopTimeParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by doki on 22.10.16.
 */
public class App
{
    /**
     * 
     * @param args [0] - sdng.log, [1] - gc.log, [2] - top, [3] - dbName, [4] timezone
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException
    {
        String log = args[0];
        String mode = System.getProperty("parse.mode", "");

        ITimeParser timeParser = buildTimeParser(log, mode);
        IDataParser dataParser = buildDataParser(mode);

        configureTimeZone(args, timeParser);

        try (InfluxDAOWorker influxDAOWorker = buildDaoWorker(args)) {
            parseEntries(log, timeParser, dataParser, influxDAOWorker);
        }

        if (System.getProperty("NoCsv") == null)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }
    }
    private static InfluxDAOWorker buildDaoWorker(String[] args) {
        InfluxDAOWorker influxDAOWorker = null;
        if (args.length > 1) {
            influxDAOWorker = new InfluxDAOWorker(args[1]);
            influxDAOWorker.init();
        }
        return influxDAOWorker;
    }

    private static void configureTimeZone(String[] args, ITimeParser parser) {
        if (args.length > 2)
        {
            parser.configureTimeZone(args[2]);
        }
    }

    private static void parseEntries(String log, ITimeParser timeParser, IDataParser dataParser,
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

    private static ITimeParser buildTimeParser(String log, String parseMode) {
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

    private static IDataParser buildDataParser(String parseMode) {
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
