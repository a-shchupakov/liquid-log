package ru.naumen.sd40.log.parser.storages.dataSets;

import ru.naumen.sd40.log.parser.storages.TopDataStorage;

public class TopDataSet implements IDataSet {
    private TopDataStorage topDataStorage = new TopDataStorage();

    public TopDataStorage get() {
        return topDataStorage;
    }
}
