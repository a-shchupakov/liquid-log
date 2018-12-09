package ru.naumen.sd40.log.parser.timeParser;

import org.junit.Assert;
import org.junit.Test;
import ru.naumen.sd40.log.parser.parseMods.loader.data.LoaderDataSet;
import ru.naumen.sd40.log.parser.parseMods.loader.parser.data.LoaderDataParser;
import ru.naumen.sd40.log.parser.parseMods.loader.parser.time.LoaderTimeParser;

import java.text.ParseException;

public class LoaderParserTests extends Assert {
    @Test
    public void parseTimeTest() {
        String example = "102725313 1970-01-01 00:00:00,555 [http-nio-8443-exec-83 operator1 fs000080000m0jaoh10o2ito00] DEBUG AdvFormEngine - session: fs000080000m0jaoh10o2ito00 render time: 18";
        LoaderTimeParser timeParser = new LoaderTimeParser();

        long actual = 0;
        long expected = 555;
        try {
            actual = timeParser.parseLine(example);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void parseDataTest() {
        String example = "oh10o2ito00 render time: 18";
        LoaderDataParser dataParser = new LoaderDataParser();
        LoaderDataSet dataSet = new LoaderDataSet();

        dataParser.parseLine(example, dataSet);

        double actual = dataSet.getMaxRenderTime();
        double expected = 18;

        assertEquals(actual, expected, 0.0001);
    }
}
