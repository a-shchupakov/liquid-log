package ru.naumen.sd40.log.parser.parseMods.sdng.data;

import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

public class SdngDataSet implements IDataSet {
    private ActionDataSet actionDataSet = new ActionDataSet();
    private ErrorDataSet errorDataSet = new ErrorDataSet();

    public ActionDataSet getAction() {
        return actionDataSet;
    }

    public ErrorDataSet getError() {
        return errorDataSet;
    }
}
