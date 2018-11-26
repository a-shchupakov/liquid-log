package ru.naumen.sd40.log.parser.storages.dataSets;

import ru.naumen.sd40.log.parser.storages.GcDataStorage;

public class GcDataSet implements IDataSet {
    private GcDataStorage gcDataStorage = new GcDataStorage();

    public GcDataStorage get() {
        return gcDataStorage;
    }
}
