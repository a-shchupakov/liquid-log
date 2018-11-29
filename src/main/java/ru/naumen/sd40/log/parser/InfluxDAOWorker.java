package ru.naumen.sd40.log.parser;

import ru.naumen.perfhouse.influx.IDataBase;
import ru.naumen.sd40.log.parser.parseMods.dataSets.GcDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.IDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.SdngDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.TopDataSet;
import ru.naumen.sd40.log.parser.parseMods.dataSets.factories.DataSetFactory;

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
            saveToDB();
        }
        currentDataSet = null;
    }

    private void saveToDB() {
        if (currentDataSet instanceof SdngDataSet)
            saveToDB((SdngDataSet) currentDataSet);
        else if (currentDataSet instanceof TopDataSet)
            saveToDB((TopDataSet) currentDataSet);
        else if (currentDataSet instanceof GcDataSet)
            saveToDB((GcDataSet) currentDataSet);
    }

    private void saveToDB(SdngDataSet dataSet) {
        influxStorage.storeSdng(influxDb, currentKey, dataSet, traceResult);
    }

    private void saveToDB(TopDataSet dataSet) {
        influxStorage.storeTop(influxDb, currentKey, dataSet, traceResult);
    }

    private void saveToDB(GcDataSet dataSet) {
        influxStorage.storeGc(influxDb, currentKey, dataSet, traceResult);
    }
}
