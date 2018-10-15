package ru.naumen.sd40.log.parser.storages;

public class SdngDataStorage implements IDataStorage {
    private ActionDataStorage actionDataStorage;
    private ErrorDataStorage errorDataStorage;

    public SdngDataStorage() {
        actionDataStorage = new ActionDataStorage();
        errorDataStorage = new ErrorDataStorage();
    }
    public ActionDataStorage getActionDataStorage() {
        return actionDataStorage;
    }

    public ErrorDataStorage getErrorDataStorage() {
        return errorDataStorage;
    }
}
