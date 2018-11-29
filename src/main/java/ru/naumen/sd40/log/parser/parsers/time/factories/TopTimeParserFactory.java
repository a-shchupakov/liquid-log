package ru.naumen.sd40.log.parser.parsers.time.factories;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.TopTimeParser;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TopTimeParserFactory implements TimeParserFactory {
    private TopTimeParser timeParser = new TopTimeParser();

    @Override
    public ITimeParser create() {
        return timeParser;
    }
}
