package ru.naumen.sd40.log.parser.parseMods;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.storages.dataSets.factories.DataSetFactory;

@Component
public interface ParseMode {
    ITimeParser getTimeParser();
    DataSetFactory getDataSetFactory();
    IDataParser getDataParser();
}
