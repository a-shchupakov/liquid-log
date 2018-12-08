package ru.naumen.sd40.log.parser.parseMods.sdng;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.DataType;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.sdng.data.SdngDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.sdng.parser.data.SdngDataParser;
import ru.naumen.sd40.log.parser.parseMods.sdng.parser.time.SdngTimeParserFactory;
import ru.naumen.sd40.log.parser.parseMods.ParsingUtils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("sdng")
public class SdngParseMode implements ParseMode {
    private SdngTimeParserFactory timeParserFactory;
    private SdngDataSetFactory dataSetFactory;
    private SdngDataParser dataParser;

    @Autowired
    public SdngParseMode(SdngTimeParserFactory timeParserFactory,
                         SdngDataSetFactory dataSetFactory,
                         SdngDataParser dataParser) {
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
        return SdngDataType.values();
    }

    public enum SdngDataType implements DataType {
        RESPONSE(ResponseTimes.getProps(), ResponseTimes.getNames(), ResponseTimes.getUnits(), ResponseTimes.TITLE),
        ACTIONS(PerformedActions.getProps(), PerformedActions.getNames(), PerformedActions.getUnits(), PerformedActions.TITLE);

        private List<String> properties;
        private Map<String, String> names;
        private Map<String, String> units;
        private String title;

        SdngDataType(List<String> properties, Map<String, String> names, Map<String, String> units, String title) {
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

    public static class ResponseTimes {
        private ResponseTimes() { }

        public static final String TITLE = "Response times";

        public static final String PERCENTILE50 = "percent50";
        public static final String PERCENTILE95 = "percent95";
        public static final String PERCENTILE99 = "percent99";
        public static final String PERCENTILE999 = "percent999";
        public static final String MAX = "max";
        public static final String MIN = "min";
        public static final String COUNT = "count";
        public static final String ERRORS = "errors";
        public static final String MEAN = "mean";
        public static final String STDDEV = "stddev";

        private static final String PERCENTILE50_FULL_NAME = "50";
        private static final String PERCENTILE95_FULL_NAME = "95";
        private static final String PERCENTILE99_FULL_NAME = "99";
        private static final String PERCENTILE999_FULL_NAME = "99.9";
        private static final String MAX_FULL_NAME = "Max";
        private static final String MIN_FULL_NAME = "Min";
        private static final String COUNT_FULL_NAME = "Count";
        private static final String ERRORS_FULL_NAME = "Errors";
        private static final String MEAN_FULL_NAME = "Mean";
        private static final String STDDEV_FULL_NAME = "Stddev";

        private static final String PERCENTILE50_UNIT = "%";
        private static final String PERCENTILE95_UNIT = "%";
        private static final String PERCENTILE99_UNIT = "%";
        private static final String PERCENTILE999_UNIT = "%";
        private static final String MAX_UNIT = "";
        private static final String MIN_UNIT = "";
        private static final String COUNT_UNIT = "";
        private static final String ERRORS_UNIT = "";
        private static final String MEAN_UNIT = "";
        private static final String STDDEV_UNIT = "";

        private static final Map<String, String> names;
        private static final Map<String, String> units;

        static {
            names = new HashMap<>();
            units = new HashMap<>();

            names.put(PERCENTILE50, PERCENTILE50_FULL_NAME);
            names.put(PERCENTILE95, PERCENTILE95_FULL_NAME);
            names.put(PERCENTILE99, PERCENTILE99_FULL_NAME);
            names.put(PERCENTILE999, PERCENTILE999_FULL_NAME);
            names.put(MAX, MAX_FULL_NAME);
            names.put(MIN, MIN_FULL_NAME);
            names.put(COUNT, COUNT_FULL_NAME);
            names.put(ERRORS, ERRORS_FULL_NAME);
            names.put(MEAN, MEAN_FULL_NAME);
            names.put(STDDEV, STDDEV_FULL_NAME);

            units.put(PERCENTILE50, PERCENTILE50_UNIT);
            units.put(PERCENTILE95, PERCENTILE95_UNIT);
            units.put(PERCENTILE99, PERCENTILE99_UNIT);
            units.put(PERCENTILE999, PERCENTILE999_UNIT);
            units.put(MAX, MAX_UNIT);
            units.put(MIN, MIN_UNIT);
            units.put(COUNT, COUNT_UNIT);
            units.put(ERRORS, ERRORS_UNIT);
            units.put(MEAN, MEAN_UNIT);
            units.put(STDDEV, STDDEV_UNIT);
        }

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                    PERCENTILE999, MAX, MIN);
        }

        static Map<String, String> getNames() {
            return names;
        }

        static Map<String, String> getUnits() {
            return units;
        }
    }

    public static class PerformedActions {
        private PerformedActions() { }
        public static final String TITLE = "Performed actions";

        public static final String ADD_ACTIONS = "addActions";
        public static final String EDIT_ACTIONS = "editActions";
        public static final String LIST_ACTIONS = "listActions";
        public static final String COMMENT_ACTIONS = "commentActions";
        public static final String GET_FORM_ACTIONS = "getFormActions";
        public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
        public static final String SEARCH_ACTIONS = "searchActions";
        public static final String ACTIONS_COUNT = "count";
        public static final String GET_CATALOG_ACTIONS = "getCatalogActions";

        private static final String ADD_ACTIONS_FULL_NAME = "AddObject";
        private static final String EDIT_ACTIONS_FULL_NAME = "EditObject";
        private static final String LIST_ACTIONS_FULL_NAME = "GetList";
        private static final String COMMENT_ACTIONS_FULL_NAME = "Comment";
        private static final String GET_FORM_ACTIONS_FULL_NAME = "GetForm";
        private static final String GET_DT_OBJECT_ACTIONS_FULL_NAME = "GetDtObject";
        private static final String SEARCH_ACTIONS_FULL_NAME = "Search";
        private static final String ACTIONS_COUNT_FULL_NAME = "Summary";
        private static final String GET_CATALOG_ACTIONS_FULL_NAME = "GetCatalog";

        private static final String ADD_ACTIONS_UNIT = "";
        private static final String EDIT_ACTIONS_UNIT = "";
        private static final String LIST_ACTIONS_UNIT = "";
        private static final String COMMENT_ACTIONS_UNIT = "";
        private static final String GET_FORM_ACTIONS_UNIT = "";
        private static final String GET_DT_OBJECT_ACTIONS_UNIT = "";
        private static final String SEARCH_ACTIONS_UNIT = "";
        private static final String ACTIONS_COUNT_UNIT = "";
        private static final String GET_CATALOG_ACTIONS_UNIT = "";

        private static final Map<String, String> names;
        private static final Map<String, String> units;

        static {
            names = new HashMap<>();
            units = new HashMap<>();

            names.put(ADD_ACTIONS, ADD_ACTIONS_FULL_NAME);
            names.put(EDIT_ACTIONS, EDIT_ACTIONS_FULL_NAME);
            names.put(LIST_ACTIONS, LIST_ACTIONS_FULL_NAME);
            names.put(COMMENT_ACTIONS, COMMENT_ACTIONS_FULL_NAME);
            names.put(GET_FORM_ACTIONS, GET_FORM_ACTIONS_FULL_NAME);
            names.put(GET_DT_OBJECT_ACTIONS, GET_DT_OBJECT_ACTIONS_FULL_NAME);
            names.put(SEARCH_ACTIONS, SEARCH_ACTIONS_FULL_NAME);
            names.put(ACTIONS_COUNT, ACTIONS_COUNT_FULL_NAME);
            names.put(GET_CATALOG_ACTIONS, GET_CATALOG_ACTIONS_FULL_NAME);

            units.put(ADD_ACTIONS, ADD_ACTIONS_UNIT);
            units.put(EDIT_ACTIONS, EDIT_ACTIONS_UNIT);
            units.put(LIST_ACTIONS, LIST_ACTIONS_UNIT);
            units.put(COMMENT_ACTIONS, COMMENT_ACTIONS_UNIT);
            units.put(GET_FORM_ACTIONS, GET_FORM_ACTIONS_UNIT);
            units.put(GET_DT_OBJECT_ACTIONS, GET_DT_OBJECT_ACTIONS_UNIT);
            units.put(SEARCH_ACTIONS, SEARCH_ACTIONS_UNIT);
            units.put(ACTIONS_COUNT, ACTIONS_COUNT_UNIT);
            units.put(GET_CATALOG_ACTIONS, GET_CATALOG_ACTIONS_UNIT);
        }

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                    GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, SEARCH_ACTIONS, GET_CATALOG_ACTIONS);
        }

        static Map<String, String> getNames() {
            return names;
        }

        static Map<String, String> getUnits() {
            return units;
        }
    }
}
