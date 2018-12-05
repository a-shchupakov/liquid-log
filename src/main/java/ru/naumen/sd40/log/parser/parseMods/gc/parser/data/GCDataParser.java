package ru.naumen.sd40.log.parser.parseMods.gc.parser.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.gc.data.GCDataSet;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GCDataParser implements IDataParser<GCDataSet> {
    private static final Pattern gcExecutionTime = Pattern.compile(".*real=(.*)secs.*");

    @Override
    public void parseLine(String line, GCDataSet dataSet) {

        Matcher matcher = gcExecutionTime.matcher(line);
        if (matcher.find())
        {
            double value = Double.parseDouble(matcher.group(1).trim().replace(',', '.'));
            dataSet.addValue(value);
        }
    }
}
