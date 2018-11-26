package ru.naumen.sd40.log.parser.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;

@Component
public interface IDataParser<T extends IDataSet> {
    void parseLine(String line, T dataSet);
}
