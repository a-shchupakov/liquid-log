package ru.naumen.sd40.log.parser.storages.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.TopDataSet;

@Component
public class TopDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new TopDataSet();
    }
}
