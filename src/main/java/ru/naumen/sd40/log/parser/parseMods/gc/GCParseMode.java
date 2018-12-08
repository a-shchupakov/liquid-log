package ru.naumen.sd40.log.parser.parseMods.gc;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.DataType;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.gc.data.GCDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.gc.parser.data.GCDataParser;
import ru.naumen.sd40.log.parser.parseMods.gc.parser.time.GCTimeParserFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.ParsingUtils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("gc")
public class GCParseMode implements ParseMode {
    private GCTimeParserFactory timeParserFactory;
    private GCDataSetFactory dataSetFactory;
    private GCDataParser dataParser;

    @Autowired
    public GCParseMode(GCTimeParserFactory timeParserFactory,
                       GCDataSetFactory dataSetFactory,
                       GCDataParser dataParser) {
        this.timeParserFactory = timeParserFactory;
        this.dataSetFactory = dataSetFactory;
        this.dataParser = dataParser;
    }

    @Override
    public ITimeParser getTimeParser() {
        return timeParserFactory.create();
    }

    @Override
    public DataSetFactory getDataSetFactory() {
        return dataSetFactory;
    }

    @Override
    public IDataParser getDataParser() {
        return dataParser;
    }

    @Override
    public DataType[] getDataTypes() {
        return GCDataType.values();
    }

    public enum GCDataType implements DataType {
        GARBAGE_COLLECTION(GarbageCollection.getProps(), GarbageCollection.getNames(), GarbageCollection.getUnits(), GarbageCollection.TITLE);

        private List<String> properties;
        private Map<String, String> names;
        private Map<String, String> units;
        private String title;

        GCDataType(List<String> properties, Map<String, String> names, Map<String, String> units, String title) {
            this.properties = properties;
            this.names = names;
            this.units = units;
            this.title = title;
        }

        @Override
        public List<String> getTypeProperties() {
            return this.properties;
        }

        @Override
        public Map<String, String> fullNameMap() {
            return names;
        }

        @Override
        public Map<String, String> unitsMap() {
            return units;
        }

        @Override
        public String getDisplayName() {
            return title;
        }
    }


    public static class GarbageCollection {
        private GarbageCollection() { }
        public static final String TITLE = "Garbage collection";

        public static final String GCTIMES = "gcTimes";
        public static final String AVARAGE_GC_TIME = "avgGcTime";
        public static final String MAX_GC_TIME = "maxGcTime";

        private static final String GCTIMES_FULL_NAME = "GC Performed";
        private static final String AVARAGE_GC_TIME_FULL_NAME = "Average GC Time";
        private static final String MAX_GC_TIME_FULL_NAME = "Max GC Time";

        private static final String GCTIMES_UNIT = "times";
        private static final String AVARAGE_GC_TIME_UNIT = "ms";
        private static final String MAX_GC_TIME_UNIT = "ms";

        private static final Map<String, String> names;
        private static final Map<String, String> units;

        static {
            names = new HashMap<>();
            units = new HashMap<>();

            names.put(GCTIMES, GCTIMES_FULL_NAME);
            names.put(AVARAGE_GC_TIME, AVARAGE_GC_TIME_FULL_NAME);
            names.put(MAX_GC_TIME, MAX_GC_TIME_FULL_NAME);

            units.put(GCTIMES, GCTIMES_UNIT);
            units.put(AVARAGE_GC_TIME, AVARAGE_GC_TIME_UNIT);
            units.put(MAX_GC_TIME, MAX_GC_TIME_UNIT);
        }

        static List<String> getProps()
        {
            return Lists.newArrayList(Constants.TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
        }

        static Map<String, String> getNames() {
            return names;
        }

        static Map<String, String> getUnits() {
            return units;
        }
    }
}
