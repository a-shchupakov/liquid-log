package ru.naumen.sd40.log.parser.parsers.data;

import ru.naumen.sd40.log.parser.storages.IDataStorage;
import ru.naumen.sd40.log.parser.storages.TopDataStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopDataParser implements IDataParser  {
    private static final Pattern laPattern = Pattern.compile(".*load average:(.*)");
    private Pattern cpuAndMemPattern = Pattern
            .compile("^ *\\d+ \\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ +\\S+ \\S+ +(\\S+) +(\\S+) +\\S+ java");
    @Override
    public void parseLine(String line, IDataStorage dataStorage) {
        //get la
        TopDataStorage topDataStorage = (TopDataStorage) dataStorage;
        Matcher la = laPattern.matcher(line);
        if (la.find())
        {
            topDataStorage.addLa(Double.parseDouble(la.group(1).split(",")[0].trim()));
        }

        //get cpu and mem
        Matcher cpuAndMemMatcher = cpuAndMemPattern.matcher(line);
        if (cpuAndMemMatcher.find())
        {
            topDataStorage.addCpu(Double.valueOf(cpuAndMemMatcher.group(1)));
            topDataStorage.addMem(Double.valueOf(cpuAndMemMatcher.group(2)));
        }
    }
}