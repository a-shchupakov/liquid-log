package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.naumen.perfhouse.influx.DBMock;
import ru.naumen.sd40.log.parser.parsers.ParsingUtils;

public class InfluxDAOWorkerTests extends Assert {
    private DBMock dataBase;
    private final long firstKey = 1 * 60000;
    private final long secondKey = 5 * 60000;

    @Before
    public void setUpDao() {
        dataBase = new DBMock();
    }

    private InfluxDAOWorker getDaoWorker() {
        InfluxDAOWorker worker = new InfluxDAOWorker(dataBase, "testDataBase");
        worker.init();
        return worker;
    }
    @Test
    public void testGetDataSet() {
        InfluxDAOWorker daoWorker = getDaoWorker();

        daoWorker.getDataSet(ParsingUtils.roundToFiveMins(firstKey));
        daoWorker.getDataSet(ParsingUtils.roundToFiveMins(secondKey));
        assertEquals(1, dataBase.getEntriesCount());

        daoWorker.close();
        assertEquals(2, dataBase.getEntriesCount());
    }

}
