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

import java.util.List;

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

    public enum SdngDataType implements DataType {
        RESPONSE(ResponseTimes.getProps()),
        ACTIONS(PerformedActions.getProps());

        private List<String> properties;

        SdngDataType(List<String> properties)
        {
            this.properties = properties;
        }

        @Override
        public List<String> getTypeProperties()
        {
            return this.properties;
        }
    }

    public static class ResponseTimes {
        private ResponseTimes() { }

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

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, COUNT, ERRORS, MEAN, STDDEV, PERCENTILE50, PERCENTILE95, PERCENTILE99,
                    PERCENTILE999, MAX, MIN);
        }
    }

    public static class PerformedActions {
        private PerformedActions() {

        }

        public static final String ADD_ACTIONS = "addActions";
        public static final String EDIT_ACTIONS = "editActions";
        public static final String LIST_ACTIONS = "listActions";
        public static final String COMMENT_ACTIONS = "commentActions";
        public static final String GET_FORM_ACTIONS = "getFormActions";
        public static final String GET_DT_OBJECT_ACTIONS = "getDtObjectActions";
        public static final String SEARCH_ACTIONS = "searchActions";
        public static final String ACTIONS_COUNT = "count";
        public static final String GET_CATALOG_ACTIONS = "getCatalogActions";

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, ADD_ACTIONS, EDIT_ACTIONS, LIST_ACTIONS, COMMENT_ACTIONS, ACTIONS_COUNT,
                    GET_FORM_ACTIONS, GET_DT_OBJECT_ACTIONS, SEARCH_ACTIONS, GET_CATALOG_ACTIONS);
        }

    }
}
