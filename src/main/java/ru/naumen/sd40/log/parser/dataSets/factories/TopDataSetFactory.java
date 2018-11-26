package ru.naumen.sd40.log.parser.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.dataSets.TopDataSet;
import ru.naumen.sd40.log.parser.dataSets.IDataSet;

@Component
public class TopDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new TopDataSet();
    }
}
