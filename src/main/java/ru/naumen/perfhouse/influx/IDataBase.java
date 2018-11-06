package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.storages.DataSet;

public interface IDataBase {
    void storeData(String dbName, long date, DataSet dataSet);
    void init();
    void connectToDB(String dbName);
}
