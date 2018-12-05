package ru.naumen.sd40.log.parser.parseMods;

import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;

import java.util.List;

public interface ParseMode {
    ITimeParser getTimeParser();
    DataSetFactory getDataSetFactory();
    IDataParser getDataParser();
}
