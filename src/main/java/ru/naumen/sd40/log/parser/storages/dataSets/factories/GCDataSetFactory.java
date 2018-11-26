package ru.naumen.sd40.log.parser.storages.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.GcDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;

@Component("gc" + "DataSetFactory")
public class GCDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new GcDataSet();
    }
}
