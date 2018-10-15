package ru.naumen.sd40.log.parser.parsers.time;

import java.text.ParseException;

public interface ITimeParser {
    long parseLine(String line) throws ParseException;
    void configureTimeZone(String zone);
}
