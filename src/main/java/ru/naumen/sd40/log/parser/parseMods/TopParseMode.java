package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.TopDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.TopDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.factories.TopTimeParserFactory;

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
