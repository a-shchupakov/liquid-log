package ru.naumen.sd40.log.parser.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.dataSets.TopDataSet;

@Component("top" + "DataSetFactory")
public class TopDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new TopDataSet();
    }
}
