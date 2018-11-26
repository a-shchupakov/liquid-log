package ru.naumen.sd40.log.parser.parsers.time.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;

@Component
public interface TimeParserFactory {
    ITimeParser create();
}
