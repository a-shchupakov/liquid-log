package ru.naumen.perfhouse.statdata;

import ru.naumen.perfhouse.statdata.Constants.GarbageCollection;
import ru.naumen.perfhouse.statdata.Constants.PerformedActions;
import ru.naumen.perfhouse.statdata.Constants.ResponseTimes;
import ru.naumen.perfhouse.statdata.Constants.Top;

import java.util.List;

public enum DataType
{
    //@formatter:off
    RESPONSE(ResponseTimes.getProps()),
    GARBAGE_COLLECTION(GarbageCollection.getProps()),
    ACTIONS(PerformedActions.getProps()),
    TOP(Top.getProps());
    //@formtatter:on
    
    private List<String> properties;

    DataType(List<String> properties)
    {
        this.properties = properties;
    }

    List<String> getTypeProperties()
    {
        return this.properties;
    }
}
