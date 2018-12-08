package ru.naumen.sd40.log.parser.parseMods.loader.parser.time;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.naumen.sd40.log.parser.parseMods.interfaces.ITimeParser;
import ru.naumen.sd40.log.parser.parseMods.interfaces.TimeParserFactory;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoaderTimeParserFactory implements TimeParserFactory {
    private LoaderTimeParser timeParser = new LoaderTimeParser();

    @Override
    public ITimeParser create() {
        return timeParser;
    }
}
