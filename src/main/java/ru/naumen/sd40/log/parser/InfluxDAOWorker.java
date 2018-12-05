package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.IDataBase;
import ru.naumen.sd40.log.parser.parseMods.gc.data.GcDataSet;
import ru.naumen.sd40.log.parser.parseMods.interfaces.DataSetFactory;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;
import ru.naumen.sd40.log.parser.parseMods.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.parseMods.top.data.TopDataSet;

import java.io.Closeable;

public class InfluxDAOWorker implements Closeable {
    private String influxDb;
    private IDataBase influxStorage;
    private long currentKey = -1;
    private IDataSet currentDataSet;
    private boolean traceResult;
    private DataSetFactory dataSetFactory;

    public InfluxDAOWorker(IDataBase dataBase, boolean traceResult, DataSetFactory dataSetFactory) {
        influxStorage = dataBase;
        this.traceResult = traceResult;
        this.dataSetFactory = dataSetFactory;
    }

    public void init(String dbName) {
        influxDb = dbName.replaceAll("-", "_");
        influxStorage.init();
        influxStorage.connectToDB(influxDb);
    }

    public IDataSet getDataSet(long key) {
        if (currentKey == key) {
            return currentDataSet;
        }
        if (currentKey != -1)
            saveToDB();
        currentDataSet = dataSetFactory.create();
        currentKey = key;
        return currentDataSet;
    }

    @Override
    public void close() {
        if (currentDataSet != null) {
            // TODO: trace result here
            saveToDB();
        }
        currentDataSet = null;
    }

    private void saveToDB() {
        influxStorage.storeData(influxDb, currentKey, currentDataSet);
    }
}
