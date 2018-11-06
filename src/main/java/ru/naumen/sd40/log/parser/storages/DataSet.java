package ru.naumen.sd40.log.parser.storages;

import ru.naumen.sd40.log.parser.storages.ActionDataStorage;
import ru.naumen.sd40.log.parser.storages.ErrorDataStorage;
import ru.naumen.sd40.log.parser.storages.GcDataStorage;
import ru.naumen.sd40.log.parser.storages.TopDataStorage;

/**
 * Created by doki on 22.10.16.
 */
public class DataSet
{
    private ActionDataStorage actionsData = new ActionDataStorage();
    private ErrorDataStorage errorData = new ErrorDataStorage();
    private GcDataStorage gcData = new GcDataStorage();
    private TopDataStorage cpuData = new TopDataStorage();


    public ActionDataStorage getActionsData()
    {
        return actionsData;
    }

    public ErrorDataStorage getErrorData()
    {
        return errorData;
    }

    public GcDataStorage getGcData()
    {
        return gcData;
    }

    public TopDataStorage getCpuData()
    {
        return cpuData;
    }
}