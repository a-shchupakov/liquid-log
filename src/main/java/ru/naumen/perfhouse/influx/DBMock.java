package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parsers.data.DataSet;

import java.util.HashMap;

public class DBMock implements IDataBase {
    private String dbName;
    private HashMap<Long, DataSet> entries;

    public int getEntriesCount(){
        return entries.size();
    }

    public DBMock() {
        entries = new HashMap<>();
    }

    @Override
    public void storeData(String dbName, long date, DataSet dataSet, boolean traceResult) {
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
