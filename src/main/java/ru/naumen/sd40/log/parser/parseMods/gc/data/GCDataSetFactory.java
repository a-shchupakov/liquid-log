package ru.naumen.sd40.log.parser.parseMods.gc.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

@Component
public class GCDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new GcDataSet();
    }
}
