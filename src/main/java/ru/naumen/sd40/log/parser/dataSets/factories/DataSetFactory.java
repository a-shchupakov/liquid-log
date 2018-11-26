package ru.naumen.sd40.log.parser.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.dataSets.IDataSet;

@Component
public interface DataSetFactory {
    IDataSet create();
}
