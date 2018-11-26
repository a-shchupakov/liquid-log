package ru.naumen.sd40.log.parser.storages.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.storages.dataSets.SdngDataSet;

@Component
public class SdngDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new SdngDataSet();
    }
}
