package ru.naumen.sd40.log.parser.storages.dataSets;

import ru.naumen.sd40.log.parser.storages.GСDataStorage;

public class GcDataSet implements IDataSet {
    private GСDataStorage gcDataStorage = new GСDataStorage();

    public GСDataStorage get() {
        return gcDataStorage;
    }
}
