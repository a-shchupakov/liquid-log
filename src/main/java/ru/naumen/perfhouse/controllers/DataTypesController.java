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
import ru.naumen.sd40.log.parser.parseMods.ParsingUtils;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("Duplicates") //TODO: handle it
@Controller
public class DataTypesController {
    private StatDataService service;
    private Map<String, ParseMode> parseModes;
    private static final String DATA_TYPES = "data_types";
    private static final String NO_HISTORY_VIEW = "no_history";
    private static final String GRAPHICS = "graphics";

    @Inject
    public DataTypesController(Map<String, ParseMode> parseModes, StatDataService service)
    {
        this.service = service;
        this.parseModes = parseModes;
    }

    @RequestMapping(path = "/data_types/{client}")
    public ModelAndView indexByDay(@PathVariable("client") String client) throws ParseException
    {
        //TODO: здесь year moth day custom приходят в requestParam
        Map<String, Object> model = new HashMap<>();
        model.put("client", client);
        putDataTypes(model);
        return new ModelAndView(DATA_TYPES, model, HttpStatus.OK);
    }

    @RequestMapping(path = "/graphics/{client}/{year}/{month}/{dataType}")
    public ModelAndView indexByMonth(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                     @PathVariable(name = "year", required = false) int year,
                                     @PathVariable(name = "month", required = false) int month) throws ParseException
    {
        String redirect = String.format("/graphics/%s/%d/%d/%d/%s", client, year, month, 0, dataType);
        return new ModelAndView("forward:" + redirect);
    }

    @RequestMapping(path = "/graphics/{client}/{year}/{month}/{day}/{dataType}")
    public ModelAndView getGraphic(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                   @PathVariable(name = "year", required = false) Integer year,
                                   @PathVariable(name = "month", required = false) Integer month,
                                   @PathVariable(name = "day", required = false) Integer day) throws ParseException
    {
        DataType dataTypeEnum = getDataTypeFromString(dataType);
        boolean compress = day == 0;

        return getDataAndViewByDate(client, dataTypeEnum, year, month, day, GRAPHICS, compress);
    }

    @RequestMapping(path = "/graphics/{client}/custom/{dataType}")
    public ModelAndView getGraphic(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                  @RequestParam("from") String from,
                                  @RequestParam("to") String to, @RequestParam("maxResults") int count) throws ParseException
    {
        DataType dataTypeEnum = getDataTypeFromString(dataType);

        return getDataAndViewCustom(client, dataTypeEnum, from, to, count, GRAPHICS);
    }

    @RequestMapping(path = "/graphics/{client}/{dataType}")
    public ModelAndView indexLast864(@PathVariable("client") String client, @PathVariable("dataType") String dataType,
                                  @RequestParam(name = "count", defaultValue = "864") int count) throws ParseException
    {
        DataType dataTypeEnum = getDataTypeFromString(dataType);

        return getDataAndView(client, dataTypeEnum, count, GRAPHICS);
    }

    private ModelAndView getDataAndView(String client, DataType dataType, int count, String viewName)
            throws ParseException
    {
        StatData data = service.getData(client, dataType, count);
        if (data == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("data", data.asModel());
        model.put("fullNames", dataType.fullNameMap());
        model.put("units", dataType.unitsMap());
        model.put("dataTypeName", ParsingUtils.stringifyDataType(dataType));
        model.put("title", dataType.getDisplayName());

        model.put("client", client);

        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewByDate(String client, DataType type, int year, int month, int day,
                                              String viewName, boolean compress) throws ParseException
    {
        StatData dataDate = service.getDataDate(client, type, year, month, day);
        if (dataDate == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }

        dataDate = compress ? service.compress(dataDate, 3 * 60 * 24 / 5) : dataDate;
        Map<String, Object> model = new HashMap<>();
        model.put("data", dataDate.asModel());
        model.put("fullNames", type.fullNameMap());
        model.put("units", type.unitsMap());
        model.put("dataTypeName", ParsingUtils.stringifyDataType(type));
        model.put("title", type.getDisplayName());

        model.put("client", client);
        model.put("year", year);
        model.put("month", month);
        model.put("day", day);

        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private ModelAndView getDataAndViewCustom(String client, DataType dataType, String from, String to, int maxResults,
                                              String viewName) throws ParseException
    {
        StatData data = service.getDataCustom(client, dataType, from, to);
        if (data == null) {
            return new ModelAndView(NO_HISTORY_VIEW);
        }
        data = service.compress(data, maxResults);
        Map<String, Object> model = new HashMap<>();
        model.put("data", data.asModel());
        model.put("fullNames", dataType.fullNameMap());
        model.put("units", dataType.unitsMap());
        model.put("dataTypeName", ParsingUtils.stringifyDataType(dataType));
        model.put("title", dataType.getDisplayName());

        model.put("client", client);
        model.put("custom", true);
        model.put("from", from);
        model.put("to", to);
        model.put("maxResults", maxResults);

        return new ModelAndView(viewName, model, HttpStatus.OK);
    }

    private void putDataTypes(Map<String, Object> model) {
        List<DataType> dataTypes = new ArrayList<>();
        parseModes.values().forEach(parseMode -> dataTypes.addAll(new ArrayList<>(Arrays.asList(parseMode.getDataTypes()))));
        dataTypes.sort(Comparator.comparing(Object::toString));
        model.put("dataTypes", dataTypes);
    }

    private DataType getDataTypeFromString(String dataType) {
        for (ParseMode parseMode: parseModes.values()) {
            DataType[] dataTypes = parseMode.getDataTypes();
            for (DataType type: dataTypes) {
                if (type.toString().equals(dataType))
                    return type;
            }
        }
        return null;
    }
}
