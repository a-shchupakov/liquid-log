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

import java.util.List;

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
        TOP(Top.getProps());

        private List<String> properties;

        TopDataType(List<String> properties) {
            this.properties = properties;
        }

        @Override
        public List<String> getTypeProperties() {
            return this.properties;
        }
    }

    public static class Top {
        private Top() { }

        public static final String AVG_LA = "avgLa";
        public static final String AVG_CPU = "avgCpu";
        public static final String AVG_MEM = "avgMem";
        public static final String MAX_LA = "maxLa";
        public static final String MAX_CPU = "maxCpu";
        public static final String MAX_MEM = "maxMem";

        static List<String> getProps() {
            return Lists.newArrayList(Constants.TIME, AVG_LA, AVG_CPU, AVG_MEM, MAX_LA, MAX_CPU, MAX_MEM);
        }
    }
}
