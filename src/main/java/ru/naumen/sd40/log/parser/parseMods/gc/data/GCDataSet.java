package ru.naumen.sd40.log.parser.parseMods.gc.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

import java.util.HashMap;
import java.util.Map;

import static ru.naumen.sd40.log.parser.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.NumberUtils.roundToTwoPlaces;
import static ru.naumen.sd40.log.parser.parseMods.gc.GCParseMode.GarbageCollection.*;

public class GCDataSet implements IDataSet {
    private DescriptiveStatistics ds = new DescriptiveStatistics();

    public void addValue(double value){
        ds.addValue(value);
    }

    public double getCalculatedAvg()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMean()));
    }

    public long getGcTimes()
    {
        return ds.getN();
    }

    public double getMaxGcTime()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMax()));
    }

    public boolean isNaN()
    {
        return getGcTimes() == 0;
    }

    @Override
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new HashMap<>();

        if (!isNaN()) {
            records.put(GCTIMES, getGcTimes());
            records.put(AVARAGE_GC_TIME, getCalculatedAvg());
            records.put(MAX_GC_TIME, getMaxGcTime());
        }

        return records;
    }
}
