package ru.naumen.sd40.log.parser.parseMods.dataSets.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.SdngDataSet;

@Component
public class SdngDataSetFactory implements DataSetFactory {
    @Override
    public IDataSet create() {
        return new SdngDataSet();
    }
}
