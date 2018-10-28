package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.IDataBase;
import ru.naumen.perfhouse.influx.InfluxDAO;
import ru.naumen.sd40.log.parser.parsers.data.DataSet;

import java.io.Closeable;

public class InfluxDAOWorker implements Closeable {
    private String influxDb;
    private IDataBase influxStorage;
    private long currentKey;
    private DataSet currentDataSet;

    public InfluxDAOWorker(String dbName) {
        influxDb = dbName.replaceAll("-", "_");
        influxStorage = new InfluxDAO(System.getProperty("influx.host"), System.getProperty("influx.user"),
                System.getProperty("influx.password"));
    }

    public InfluxDAOWorker(IDataBase dataBase, String dbName) {
        influxStorage = dataBase;
        this.influxDb = dbName;
    }

    public void init() {
        influxStorage.init();
        influxStorage.connectToDB(influxDb);
    }

    public DataSet getDataSet(long key) {
        if (key != currentKey) {
            saveToDB();
            currentDataSet = new DataSet();
        }
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
        influxStorage.storeData(influxDb, currentKey, currentDataSet);
    }
}
