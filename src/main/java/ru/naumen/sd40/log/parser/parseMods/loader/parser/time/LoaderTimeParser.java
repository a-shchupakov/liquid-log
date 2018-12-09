package ru.naumen.sd40.log.parser.parseMods.loader.parser.time;

import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoaderTimeParser implements ITimeParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",
            new Locale("ru", "RU"));

    private static final Pattern PATTERN = Pattern
            .compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}");

    public LoaderTimeParser()
    {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public long parseLine(String line) throws ParseException {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find())
        {
            Date parse = DATE_FORMAT.parse(matcher.group(0));
            return parse.getTime();
        }
        return 0L;
    }

    @Override
    public void configureTimeZone(String zone) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(zone));
    }
}
