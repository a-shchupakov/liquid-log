package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.data.TopDataParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.factories.TopTimeParserFactory;
import ru.naumen.sd40.log.parser.dataSets.factories.DataSetFactory;
import ru.naumen.sd40.log.parser.dataSets.factories.TopDataSetFactory;

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
}
