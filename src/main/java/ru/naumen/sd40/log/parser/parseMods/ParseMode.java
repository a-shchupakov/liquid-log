package ru.naumen.sd40.log.parser.parseMods;

import ru.naumen.sd40.log.parser.parsers.data.IDataParser;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.dataSets.factories.DataSetFactory;

public interface ParseMode {
    ITimeParser getTimeParser();
    DataSetFactory getDataSetFactory();
    IDataParser getDataParser();
}
