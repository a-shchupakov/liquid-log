package ru.naumen.sd40.log.parser.parseMods.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.IDataSet;

@Component
public interface DataSetFactory {
    IDataSet create();
}
