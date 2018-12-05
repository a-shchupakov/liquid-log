package ru.naumen.sd40.log.parser.parseMods.sdng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.sdng.data.SdngDataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.sdng.parser.data.SdngDataParser;
import ru.naumen.sd40.log.parser.parseMods.sdng.parser.time.SdngTimeParserFactory;

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
