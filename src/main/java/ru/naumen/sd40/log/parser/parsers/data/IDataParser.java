package ru.naumen.sd40.log.parser.parsers.data;

import ru.naumen.sd40.log.parser.storages.DataSet;

public interface IDataParser {
    void parseLine(String line, DataSet dataSet);
}
