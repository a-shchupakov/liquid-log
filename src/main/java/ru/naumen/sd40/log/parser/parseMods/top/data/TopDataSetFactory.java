package ru.naumen.sd40.log.parser.parseMods.top.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

@Component
public class TopDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new TopDataSet();
    }
}
