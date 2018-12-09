package ru.naumen.sd40.log.parser.parseMods.loader.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

import java.util.HashMap;
import java.util.Map;

import static ru.naumen.sd40.log.parser.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.NumberUtils.roundToTwoPlaces;
import static ru.naumen.sd40.log.parser.parseMods.loader.LoaderParseMode.Loader.*;

public class LoaderDataSet implements IDataSet {
    private DescriptiveStatistics ds = new DescriptiveStatistics();

    public void addValue(double value){
        ds.addValue(value);
    }

    public long getCount()
    {
        return ds.getN();
    }

    public double getCalculatedAvg()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMean()));
    }

    public double getMaxRenderTime()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMax()));
    }

    public double getMinRenderTime()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getMin()));
    }

    public double getPercentile90()
    {
        return roundToTwoPlaces(getSafeDouble(ds.getPercentile(90)));
    }

    public boolean isNaN()
    {
        return getCount() == 0;
    }

    @Override
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new HashMap<>();

        if (!isNaN()) {
            records.put(MAX, getMaxRenderTime());
            records.put(MIN, getMinRenderTime());
            records.put(AVG, getCalculatedAvg());
            records.put(PERCENTILE90, getPercentile90());
        }

        return records;
    }
}
