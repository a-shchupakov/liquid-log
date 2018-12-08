package ru.naumen.perfhouse.influx;

import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

public interface IDataBase {
    void init();
    void connectToDB(String dbName);
    void storeData(String dbName, long date, IDataSet dataSet);
}
