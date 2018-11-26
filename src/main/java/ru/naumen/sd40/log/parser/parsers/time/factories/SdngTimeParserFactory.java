package ru.naumen.sd40.log.parser.parsers.time.factories;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.naumen.sd40.log.parser.parsers.time.ITimeParser;
import ru.naumen.sd40.log.parser.parsers.time.SdngTimeParser;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SdngTimeParserFactory implements TimeParserFactory {
    private SdngTimeParser timeParser = new SdngTimeParser();

    @Override
    public ITimeParser create() {
        return timeParser;
    }
}
