package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;

@Component
public interface ParseMode {
    ITimeParser getTimeParser();
    DataSetFactory getDataSetFactory();
    IDataParser getDataParser();
}
