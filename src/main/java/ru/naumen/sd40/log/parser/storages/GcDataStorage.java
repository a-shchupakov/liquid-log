package ru.naumen.sd40.log.parser.storages;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static ru.naumen.sd40.log.parser.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.NumberUtils.roundToTwoPlaces;

public class GcDataStorage implements IDataStorage {
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

    public boolean isNan()
    {
        return getGcTimes() == 0;
    }

}
