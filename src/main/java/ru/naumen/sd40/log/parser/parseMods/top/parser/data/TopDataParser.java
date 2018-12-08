package ru.naumen.sd40.log.parser.parseMods.top.parser.data;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataParser;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TopDataParser implements IDataParser<TopDataSet> {
    private static final Pattern laPattern = Pattern.compile(".*load average:(.*)");
    private static final Pattern cpuAndMemPattern = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");
    @Override
    public void parseLine(String line, TopDataSet dataSet) {
        //get la
        Matcher la = laPattern.matcher(line);
        if (la.find())
        {
            dataSet.addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
        }

        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattern.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            dataSet.addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            dataSet.addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }
}
