package ru.naumen.perfhouse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.perfhouse.statdata.StatData;
import ru.naumen.perfhouse.statdata.StatDataService;
import ru.naumen.sd40.log.parser.parseMods.DataType;
import ru.naumen.sd40.log.parser.parseMods.ParseMode;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("Duplicates") //TODO: handle it
@Controller
public class GraphicsController {
    StatDataService service;
    private Map<String, ParseMode> parseModes;
    private DataType currentDataType = null;
    private String viewName = "graphics";

    @Inject
    public GraphicsController(Map<String, ParseMode> parseModes, StatDataService service)
    {
        this.service = service;
        this.parseModes = parseModes;
    }

    public void setCurrentDataType(DataType currentDataType) {
        this.currentDataType = currentDataType;
    }

    public DataType getCurrentDataType() {
        return currentDataType;
    }

    @RequestMapping(path = "/graphics/{client}")
    public ModelAndView indexByDay(@PathVariable("client") String client) throws ParseException
    {
        Map<String, Object> model = new HashMap<>();
        putDataTypes(model);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    @RequestMapping(path = "/graphics/{client}/custom/top")
    public ModelAndView customTop(@PathVariable("client") String client, @RequestParam("from") String from,
                                  @RequestParam("to") String to, @RequestParam("maxResults") int count) throws ParseException
    {
        Map<String, Object> model = new HashMap<>();
        putDataTypes(model);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndView(String client, DataType dataType, int count, String viewName)
            throws ParseException
    {
        StatData data = service.getData(client, dataType, count);
        if (data == null) {
//            return new ModelAndView(NO_HISTORY_VIEW);
            //TODO: no data case
        }
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);

        putDataTypes(model);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
                                              String viewName) throws ParseException
    {
        return getDataAndViewByDate(client, type, year, month, day, viewName, false);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
                                              String viewName, boolean compress) throws ParseException
    {
        ru.naumen.perfhouse.statdata.StatData dataDate = service.getDataDate(client, type, year, month, day);
        if (dataDate == null) {
//            return new ModelAndView(NO_HISTORY_VIEW);
            //TODO: no data case
        }

        dataDate = compress ? service.compress(dataDate, 3 * 60 * 24 / 5) : dataDate;
        Map<String, Object> model = new HashMap<>(dataDate.asModel());
        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);

        putDataTypes(model);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewCustom(String client, DataType dataType, String from, String to, int maxResults,
                                              String viewName) throws ParseException
    {
        StatData data = service.getDataCustom(client, dataType, from, to);
        if (data == null) {
//            return new ModelAndView(NO_HISTORY_VIEW);
            //TODO: no data case
        }
        data = service.compress(data, maxResults);
        Map<String, Object> model = new HashMap<>(data.asModel());
        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);

        putDataTypes(model);
        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private void putDataTypes(Map<String, Object> model) {
        List<DataType> dataTypes = new ArrayList<>();
        parseModes.values().forEach(parseMode -> dataTypes.addAll(new ArrayList<>(Arrays.asList(parseMode.getDataTypes()))));

        model.put("dataTypes", dataTypes);
    }
}
