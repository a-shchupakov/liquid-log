package ru.naumen.sd40.log.parser.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.DataSet;

@Component
public interface IDataParser {
    void parseLine(String line, DataSet dataSet);
}
