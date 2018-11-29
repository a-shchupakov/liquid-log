package ru.naumen.sd40.log.parser.parseMods.parsers.time.factories;

import org.springframework.stereotype.Component;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;

@Component
public interface TimeParserFactory {
    ITimeParser create();
}
