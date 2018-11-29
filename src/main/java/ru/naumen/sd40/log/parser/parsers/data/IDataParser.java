package ru.naumen.sd40.log.parser.parsers.data;

import ru.naumen.sd40.log.parser.dataSets.IDataSet;

public interface IDataParser<T extends IDataSet> {
    void parseLine(String line, T dataSet);
}
