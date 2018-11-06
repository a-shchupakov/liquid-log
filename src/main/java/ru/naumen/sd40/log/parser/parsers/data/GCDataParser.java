package ru.naumen.sd40.log.parser.parsers.data;

import ru.naumen.sd40.log.parser.storages.DataSet;
import ru.naumen.sd40.log.parser.storages.GcDataStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCDataParser implements IDataParser  {
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    @Override
    public void parseLine(String line, DataSet dataSet) {
        GcDataStorage gcDataStorage = dataSet.getGcData();

        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            double value = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            gcDataStorage.addValue(value);
        }
    }
}
