package ru.naumen.perfhouse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.LogParser;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class ParseController {
    private InfluxDAO influxDAO;
    private LogParser logParser;

    @Inject
    public ParseController(InfluxDAO influxDAO, LogParser logParser)
    {
        this.influxDAO = influxDAO;
        this.logParser = logParser;
    }

    @RequestMapping(path = "/parse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void parseLogs(@RequestParam("dbName") String dbName,
                          @RequestParam("parseMode") String parseMode,
                          @RequestParam("logPath") String logPath,
                          @RequestParam(name = "timezone", required = false) String timezone,
                          @RequestParam(name = "traceResult", required = false) String traceResult) {
        boolean trace = traceResult != null;

        try {
            logParser.parseLogs(logPath, parseMode, dbName, timezone, trace, influxDAO);
        } catch (IOException | ParseException e) {

        }
    }

}
