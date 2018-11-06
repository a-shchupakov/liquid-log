package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.IDataBase;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parsers.data.DataSet;
import ru.naumen.sd40.log.parser.storages.ActionDataStorage;
import ru.naumen.sd40.log.parser.storages.ErrorDataStorage;
import ru.naumen.sd40.log.parser.storages.GcDataStorage;
import ru.naumen.sd40.log.parser.storages.TopDataStorage;

import java.io.Closeable;

public class InfluxDAOWorker implements Closeable {
    private String influxDb;
    private IDataBase influxStorage;
    private long currentKey = -1;
    private DataSet currentDataSet;
    private boolean traceResult = false;

    public InfluxDAOWorker(IDataBase dataBase, boolean traceResult) {
        influxStorage = dataBase;
        this.traceResult = traceResult;
    }

    public InfluxDAOWorker(IDataBase dataBase) {
        influxStorage = dataBase;
    }

    public void init(String dbName) {
        influxDb = dbName.replaceAll("-", "_");
        influxStorage.init();
        influxStorage.connectToDB(influxDb);
    }

    public DataSet getDataSet(long key) {
        if (currentKey == key) {
            return currentDataSet;
        }
        if (currentKey != -1)
            saveToDB();
        currentDataSet = new DataSet();
        currentKey = key;
        return currentDataSet;
    }

    @Override
    public void close() {
        if (currentDataSet != null) {
            saveToDB();
        }
        currentDataSet = null;
    }

    private void saveToDB() {
        influxStorage.storeData(influxDb, currentKey, currentDataSet, traceResult);
    }
}
