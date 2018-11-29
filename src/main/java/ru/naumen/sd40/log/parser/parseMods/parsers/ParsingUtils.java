package ru.naumen.sd40.log.parser.parseMods.parsers;

public class ParsingUtils {
    private ParsingUtils() {}
    public static long roundToFiveMins(long parsedDate) {
        int min5 = 5 * 60 * 1000;
        long count = parsedDate / min5;
        return count * min5;
    }
}
