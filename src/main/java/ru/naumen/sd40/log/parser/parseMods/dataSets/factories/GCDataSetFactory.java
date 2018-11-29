package ru.naumen.sd40.log.parser.parseMods.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.GcDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.IDataSet;

@Component
public class GCDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new GcDataSet();
    }
}
