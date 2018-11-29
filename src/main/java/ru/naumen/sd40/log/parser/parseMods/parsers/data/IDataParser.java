package ru.naumen.sd40.log.parser.parseMods.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.IDataSet;

@Component
public interface IDataParser<T extends IDataSet> {
    void parseLine(String line, T dataSet);
}
