

package ru.naumen.sd40.log.parser.parseMods.top.parser.time;

import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopTimeParser implements ITimeParser {private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm");
    private String dataDate;
    private long currentTime = 0;

    private static final Pattern timeRegex = Pattern.compile("^_+ (\\S+)");
    private static final Pattern fileRegex = Pattern.compile("\\d{8}|\\d{4}-\\d{2}-\\d{2}");

    public TopTimeParser()
    {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void configureTimeZone(String timeZone)
    {
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public void associateFile(String file) throws IllegalArgumentException {
        //Supports these masks in file name: YYYYmmdd, YYY-mm-dd i.e. 20161101, 2016-11-01
        Matcher matcher = fileRegex.matcher(file);
        if (!matcher.find())
        {
            throw new IllegalArgumentException();
        }
        this.dataDate = matcher.group(0).replaceAll("-", "");
    }

    public long parseLine(String line) throws ParseException
    {
        //check time
        Matcher matcher = timeRegex.matcher(line);
        if (matcher.find()) {
            currentTime = sdf.parse(dataDate + matcher.group(1)).getTime();
        }
        return currentTime;
    }
}
