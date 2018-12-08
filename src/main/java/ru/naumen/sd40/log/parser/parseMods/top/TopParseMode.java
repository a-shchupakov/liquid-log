package ru.naumen.sd40.log.parser.parseMods.top;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.naumen.sd40.log.parser.parseMods.DataType;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.top.parser.data.TopDataParser;
import ru.naumen.sd40.log.parser.parseMods.top.parser.time.TopTimeParserFactory;
import ru.naumen.sd40.log.parser.parseMods.ParsingUtils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("top")
public class TopParseMode implements ParseMode {
    private TopTimeParserFactory timeParserFactory;
    private TopDataSetFactory dataSetFactory;
    private TopDataParser dataParser;

    @Autowired
    public TopParseMode(TopTimeParserFactory timeParserFactory,
                        TopDataSetFactory dataSetFactory,
                        TopDataParser dataParser) {
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
        return TopDataType.values();
    }

    public enum TopDataType implements DataType {
        TOP(Top.getProps(), Top.getNames(), Top.getUnits(), Top.TITLE);

        private List<String> properties;
        private Map<String, String> names;
        private Map<String, String> units;
        private String title;

        TopDataType(List<String> properties, Map<String, String> names ,Map<String, String> units, String title) {
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

    public static class Top {
        private Top() { }
        public static final String TITLE = "Top data";

        public static final String AVG_LA = "avgLa";
        public static final String AVG_CPU = "avgCpu";
        public static final String AVG_MEM = "avgMem";
        public static final String MAX_LA = "maxLa";
        public static final String MAX_CPU = "maxCpu";
        public static final String MAX_MEM = "maxMem";

        private static final String AVG_LA_FULL_NAME = "Average LA";
        private static final String AVG_CPU_FULL_NAME = "Avarage CPU usage";
        private static final String AVG_MEM_FULL_NAME = "Average MEM usage";
        private static final String MAX_LA_FULL_NAME = "Max LA";
        private static final String MAX_CPU_FULL_NAME = "Max CPU usage";
        private static final String MAX_MEM_FULL_NAME = "Max MEM Usage";

        private static final String AVG_LA_UNITS= "";
        private static final String AVG_CPU_UNITS = "%";
        private static final String AVG_MEM_UNITS = "%";
        private static final String MAX_LA_UNITS= "";
        private static final String MAX_CPU_UNITS = "%";
        private static final String MAX_MEM_UNITS = "%";

        private static final Map<String, String> names;
        private static final Map<String, String> units;

        static {
            names = new HashMap<>();
            units = new HashMap<>();

            names.put(AVG_LA, AVG_LA_FULL_NAME);
            names.put(AVG_CPU, AVG_CPU_FULL_NAME);
            names.put(AVG_MEM, AVG_MEM_FULL_NAME);
            names.put(MAX_LA, MAX_LA_FULL_NAME);
            names.put(MAX_CPU, MAX_CPU_FULL_NAME);
            names.put(MAX_MEM, MAX_MEM_FULL_NAME);

            units.put(AVG_LA, AVG_LA_UNITS);
            units.put(AVG_CPU, AVG_CPU_UNITS);
            units.put(AVG_MEM, AVG_MEM_UNITS);
            units.put(MAX_LA, MAX_LA_UNITS);
            units.put(MAX_CPU, MAX_CPU_UNITS);
            units.put(MAX_MEM, MAX_MEM_UNITS);
        }

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
        }

        static Map<String, String> getNames() {
            return names;
        }

        static Map<String, String> getUnits() {
            return units;
        }
    }
}
