package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parseMods.gc.data.GcDataSet;
import ru.naumen.sd40.log.parser.parseMods.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSet;

public interface IDataBase {
    void init();
    void connectToDB(String dbName);
    void storeSdng(String dbName, long date, SdngDataSet dataSet, boolean traceResult);
    void storeGc(String dbName, long date, GcDataSet dataSet, boolean traceResult);
    void storeTop(String dbName, long date, TopDataSet dataSet, boolean traceResult);
}
