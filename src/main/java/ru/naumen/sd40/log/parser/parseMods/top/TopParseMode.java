package ru.naumen.sd40.log.parser.parseMods.top;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.top.parser.data.TopDataParser;
import ru.naumen.sd40.log.parser.parseMods.top.parser.time.TopTimeParserFactory;

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
