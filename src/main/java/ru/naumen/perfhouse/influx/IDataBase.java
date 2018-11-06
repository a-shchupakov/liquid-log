package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parsers.data.DataSet;

public interface IDataBase {
    void storeData(String dbName, long date, DataSet dataSet, boolean traceResult);
    void init();
    void connectToDB(String dbName);
}
