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

import java.util.List;

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
        GARBAGE_COLLECTION(GarbageCollection.getProps());

        private List<String> properties;

        GCDataType(List<String> properties) {
            this.properties = properties;
        }

        @Override
        public List<String> getTypeProperties() {
            return this.properties;
        }
    }


    public static class GarbageCollection {
        private GarbageCollection() { }

        public static final String GCTIMES = "gcTimes";
        public static final String AVARAGE_GC_TIME = "avgGcTime";
        public static final String MAX_GC_TIME = "maxGcTime";

        static List<String> getProps()
        {
            return Lists.newArrayList(Constants.TIME, GCTIMES, AVARAGE_GC_TIME, MAX_GC_TIME);
        }
    }
}
