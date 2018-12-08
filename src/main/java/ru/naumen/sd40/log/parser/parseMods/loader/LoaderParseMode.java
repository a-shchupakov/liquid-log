package ru.naumen.sd40.log.parser.parseMods.loader;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.DataType;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.ParsingUtils;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.loader.data.LoaderDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.loader.parser.data.LoaderDataParser;
import ru.naumen.sd40.log.parser.parseMods.loader.parser.time.LoaderTimeParserFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("loader")
public class LoaderParseMode implements ParseMode {
    private LoaderTimeParserFactory timeParserFactory;
    private LoaderDataSetFactory dataSetFactory;
    private LoaderDataParser dataParser;

    @Autowired
    public LoaderParseMode(LoaderTimeParserFactory timeParserFactory,
                        LoaderDataSetFactory dataSetFactory,
                        LoaderDataParser dataParser) {
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
        return  LoaderDataType.values();
    }

    public enum LoaderDataType implements DataType {
        LOADER(Loader.getProps(), Loader.getNames(), Loader.getUnits(), Loader.TITLE);

        private List<String> properties;
        private Map<String, String> names;
        private Map<String, String> units;
        private String title;

        LoaderDataType(List<String> properties, Map<String, String> names, Map<String, String> units, String title) {
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

    public static class Loader {
        private static final String TITLE = "Loader";

        public static final String MAX = "max_render_time";
        public static final String MIN = "min_render_time";
        public static final String AVG = "mean_render_time";
        public static final String PERCENTILE90 = "per90_render_time";

        private static final String MAX_FULL_NAME = "Max Render Time";
        private static final String MIN_FULL_NAME = "Min Render Time";
        private static final String AVG_FULL_NAME = "Average Render Time";
        private static final String PERCENTILE90_FULL_NAME = "Percentile 90 Render Time";

        private static final String RENDER_TIME_UNIT = "ms";

        private static final Map<String, String> names;
        private static final Map<String, String> units;

        static {
            names = new HashMap<>();
            units = new HashMap<>();

            names.put(MAX, MAX_FULL_NAME);
            names.put(MIN, MIN_FULL_NAME);
            names.put(AVG, AVG_FULL_NAME);
            names.put(PERCENTILE90, PERCENTILE90_FULL_NAME);

            units.put(MAX, RENDER_TIME_UNIT);
            units.put(MIN, RENDER_TIME_UNIT);
            units.put(AVG, RENDER_TIME_UNIT);
            units.put(PERCENTILE90, RENDER_TIME_UNIT);
        }

        static List<String> getProps()
        {
            return Lists.newArrayList(ParsingUtils.Constants.TIME, MAX, MIN, AVG, PERCENTILE90);
        }

        static Map<String, String> getNames() {
            return names;
        }

        static Map<String, String> getUnits() {
            return units;
        }
    }
}
