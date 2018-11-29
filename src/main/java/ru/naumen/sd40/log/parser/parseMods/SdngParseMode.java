package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.SdngDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.SdngDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.factories.SdngTimeParserFactory;

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
}
