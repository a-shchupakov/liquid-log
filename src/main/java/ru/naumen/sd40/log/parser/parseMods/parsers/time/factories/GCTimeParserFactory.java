package ru.naumen.sd40.log.parser.parseMods.parsers.time.factories;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.GCTimeParser;
import ru.naumen.sd40.log.parser.parseMods.parsers.time.ITimeParser;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GCTimeParserFactory implements TimeParserFactory {
    private GCTimeParser timeParser = new GCTimeParser();

    @Override
    public ITimeParser create() {
        return timeParser;
    }
}