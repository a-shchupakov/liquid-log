package ru.naumen.sd40.log.parser.parseMods.sdng.data;

import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

import java.util.HashMap;
import java.util.Map;


import static ru.naumen.sd40.log.parser.parseMods.sdng.SdngParseMode.PerformedActions.*;
import static ru.naumen.sd40.log.parser.parseMods.sdng.SdngParseMode.ResponseTimes.*;

public class SdngDataSet implements IDataSet {
    private ActionDataSet actionDataSet = new ActionDataSet();
    private ErrorDataSet errorDataSet = new ErrorDataSet();

    public ActionDataSet getAction() {
        return actionDataSet;
    }

    public ErrorDataSet getError() {
        return errorDataSet;
    }

    @Override
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new HashMap<>();

        actionDataSet.calculate();

        if (!actionDataSet.isNaN()) {
            records.put(COUNT, actionDataSet.getCount());
            records.put(MIN, actionDataSet.getMin());
            records.put(MEAN, actionDataSet.getMean());
            records.put(STDDEV, actionDataSet.getStddev());
            records.put(PERCENTILE50, actionDataSet.getPercent50());
            records.put(PERCENTILE95, actionDataSet.getPercent95());
            records.put(PERCENTILE99, actionDataSet.getPercent99());
            records.put(PERCENTILE999, actionDataSet.getPercent999());
            records.put(MAX, actionDataSet.getMax());
            records.put(ERRORS, errorDataSet.getErrorCount());
            records.put(ADD_ACTIONS, actionDataSet.getAddObjectActions());
            records.put(EDIT_ACTIONS, actionDataSet.getEditObjectsActions());
            records.put(LIST_ACTIONS, actionDataSet.getListActions());
            records.put(COMMENT_ACTIONS, actionDataSet.getCommentActions());
            records.put(GET_FORM_ACTIONS, actionDataSet.getFormActions());
            records.put(GET_DT_OBJECT_ACTIONS, actionDataSet.getDtObjectActions());
            records.put(SEARCH_ACTIONS, actionDataSet.getSearchActions());
            records.put(GET_CATALOG_ACTIONS, actionDataSet.getCatalogsActions());
        }

        return records;
    }
}
