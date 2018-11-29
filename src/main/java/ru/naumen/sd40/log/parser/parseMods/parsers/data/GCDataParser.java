package ru.naumen.sd40.log.parser.parseMods.parsers.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.dataSets.GcDataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GCDataParser implements IDataParser<GcDataSet>  {
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    @Override
    public void parseLine(String line, GcDataSet dataSet) {

        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            double value = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            dataSet.addValue(value);
        }
    }
}