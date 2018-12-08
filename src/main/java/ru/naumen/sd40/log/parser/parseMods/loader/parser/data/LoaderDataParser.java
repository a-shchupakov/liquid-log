package ru.naumen.sd40.log.parser.parseMods.loader.parser.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.loader.data.LoaderDataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoaderDataParser implements IDataParser<LoaderDataSet> {
    private static final Pattern renderTimePattern = Pattern.compile("render time: (\\d+)");

    @Override
    public void parseLine(String line, LoaderDataSet dataSet) {
        Matcher matcher = renderTimePattern.matcher(line);
        if (matcher.find())
        {
            double value = Double.parseDouble(matcher.group(1));
            dataSet.addValue(value);
        }
    }
}
