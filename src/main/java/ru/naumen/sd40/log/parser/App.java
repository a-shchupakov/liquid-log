package ru.naumen.sd40.log.parser;

import org.influxdb.dto.BatchPoints;
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
import ru.naumen.sd40.log.parser.storages.GcDataStorage;
import ru.naumen.sd40.log.parser.storages.IDataStorage;
import ru.naumen.sd40.log.parser.storages.SdngDataStorage;
import ru.naumen.sd40.log.parser.storages.TopDataStorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

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
        String influxDb = null;

        if (args.length > 1)
        {
            influxDb = args[1];
            influxDb = influxDb.replaceAll("-", "_");
        }

        InfluxDAO storage = null;
        if (influxDb != null)
        {
            storage = new InfluxDAO(System.getProperty("influx.host"), System.getProperty("influx.user"),
                    System.getProperty("influx.password"));
            storage.init();
            storage.connectToDB(influxDb);
        }

        BatchPoints points = null;
        if (storage != null)
        {
            points = storage.startBatchPoints(influxDb);
        }

        String log = args[0];
        HashMap<Long, IDataStorage> data = new HashMap<>();

        String mode = System.getProperty("parse.mode", "");

        ITimeParser timeParser = buildTimeParser(log, mode);
        IDataParser dataParser = buildDataParser(mode);

        configureTimeZone(args, timeParser);
        parseEntries(log, timeParser, dataParser, data);

        if (System.getProperty("NoCsv") == null)
        {
            System.out.print("Timestamp;Actions;Min;Mean;Stddev;50%%;95%%;99%%;99.9%%;Max;Errors\n");
        }

        saveToDB(points, data, storage, influxDb);

    }
    private static void configureTimeZone(String[] args,ITimeParser parser) {
        if (args.length > 2)
        {
            parser.configureTimeZone(args[2]);
        }
    }

    private static void parseEntries(String log, ITimeParser timeParser, IDataParser dataParser,
                                     HashMap<Long, IDataStorage> data) throws IOException, ParseException
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

                IDataStorage storage;
                if (timeParser instanceof GCTimeParser)
                    storage = data.computeIfAbsent(key, k -> new GcDataStorage());

                else if (timeParser instanceof SdngTimeParser)
                    storage = data.computeIfAbsent(key, k -> new SdngDataStorage());

                else if (timeParser instanceof TopTimeParser)
                    storage = data.computeIfAbsent(key, k -> new TopDataStorage());

                else
                    continue;

                dataParser.parseLine(line, storage);
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

    private static void saveToDB(BatchPoints points, HashMap<Long, IDataStorage> data,
                                 InfluxDAO storage, String influxDb) {
        data.forEach((k, dataStorage) -> storage.storeData(points, influxDb, k, dataStorage));
        storage.writeBatch(points);
    }
}
