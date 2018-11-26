package ru.naumen.sd40.log.parser.storages.dataSets;

import ru.naumen.sd40.log.parser.storages.ActionDataStorage;
import ru.naumen.sd40.log.parser.storages.ErrorDataStorage;

public class SdngDataSet implements IDataSet {
    private ActionDataStorage actionDataStorage = new ActionDataStorage();
    private ErrorDataStorage errorDataStorage = new ErrorDataStorage();

    public ActionDataStorage getAction() {
        return actionDataStorage;
    }

    public ErrorDataStorage getError() {
        return errorDataStorage;
    }
}
