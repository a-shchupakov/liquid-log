package ru.naumen.sd40.log.parser.parseMods.loader.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

@Component
public class LoaderDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new LoaderDataSet();
    }
}
