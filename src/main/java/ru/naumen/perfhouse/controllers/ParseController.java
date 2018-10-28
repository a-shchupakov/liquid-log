package ru.naumen.perfhouse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.naumen.sd40.log.parser.LogParser;

import java.io.IOException;
import java.text.ParseException;

@Controller
public class ParseController {

    @RequestMapping(path = "/parse", method = RequestMethod.POST)
    public String parseLogs(LogParser logParser,
                            @RequestParam("dbName") String dbName,
                            @RequestParam("parseMode") String parseMode,
                            @RequestParam("logPath") String logPath,
                            @RequestParam(name = "timezone", required = false) String timezone,
                            @RequestParam(name = "traceResult", required = false) String traceResult) {
        boolean trace = traceResult != null;
        timezone = timezone.isEmpty() ? null : timezone;
        String influxHost = "http://127.0.0.1:8086";
        String influxUser = "root";
        String influxPass = "root";

        try {
            logParser.parseLogs(logPath, parseMode, dbName, timezone, influxHost, influxUser, influxPass);
        } catch (IOException | ParseException e) {

        }

        return "descriptionView";
    }

}
