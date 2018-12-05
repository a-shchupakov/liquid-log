package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parseMods.gc.data.GcDataSet;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;
import ru.naumen.sd40.log.parser.parseMods.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSet;

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

    public void storeData(String dbName, long date, IDataSet dataSet, boolean traceResult) {
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

    @Override
    public void storeSdng(String dbName, long date, SdngDataSet dataSet, boolean traceResult) {
        storeData(dbName, date, dataSet, traceResult);
    }

    @Override
    public void storeGc(String dbName, long date, GcDataSet dataSet, boolean traceResult) {
        storeData(dbName, date, dataSet, traceResult);
    }

    @Override
    public void storeTop(String dbName, long date, TopDataSet dataSet, boolean traceResult) {
        storeData(dbName, date, dataSet, traceResult);
    }
}
