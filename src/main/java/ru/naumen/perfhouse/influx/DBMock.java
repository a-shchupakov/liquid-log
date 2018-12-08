package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

import java.util.HashMap;

public class DBMock implements IDataBase {
    private String dbName;
    private HashMap<Long, IDataSet> entries;

    public int getEntriesCount(){
        return entries.size();
    }

    public DBMock() {
        entries = new HashMap<>();
    }

    @Override
    public void storeData(String dbName, long date, IDataSet dataSet) {
        if (this.dbName.equals(dbName)) {
            entries.put(date, dataSet);
        }
    }

    @Override
    public void init() { }

    @Override
    public void connectToDB(String dbName) {
        this.dbName = dbName;
    }
}
