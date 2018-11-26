package ru.naumen.sd40.log.parser.storages.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;

@Component
public interface DataSetFactory {
    IDataSet create();
}
