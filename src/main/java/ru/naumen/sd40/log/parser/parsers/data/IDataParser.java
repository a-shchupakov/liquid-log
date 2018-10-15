package ru.naumen.sd40.log.parser.parsers.data;

import ru.naumen.sd40.log.parser.storages.IDataStorage;

public interface IDataParser {
    void parseLine(String line, IDataStorage dataStorage);
}
