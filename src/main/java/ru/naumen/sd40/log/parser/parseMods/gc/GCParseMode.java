package ru.naumen.sd40.log.parser.parseMods.gc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.gc.data.GCDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.gc.parser.data.GCDataParser;
import ru.naumen.sd40.log.parser.parseMods.gc.parser.time.GCTimeParserFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;

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
