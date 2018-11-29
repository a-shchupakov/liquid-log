package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.GCDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.GCDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.factories.GCTimeParserFactory;

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
}
