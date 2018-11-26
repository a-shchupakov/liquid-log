package ru.naumen.sd40.log.parser.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.storages.dataSets.GcDataSet;
import ru.naumen.sd40.log.parser.storages.GСDataStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GCDataParser implements IDataParser<GcDataSet>  {
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    @Override
    public void parseLine(String line, GcDataSet dataSet) {
        GСDataStorage gcDataStorage = dataSet.get();

        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            double value = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            gcDataStorage.addValue(value);
        }
    }
}
