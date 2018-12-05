package ru.naumen.sd40.log.parser.parseMods.interfaces;

public interface IDataParser<T extends IDataSet> {
    void parseLine(String line, T dataSet);
}
