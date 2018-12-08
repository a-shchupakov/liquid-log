package ru.naumen.sd40.log.parser.parseMods.top.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.naumen.sd40.log.parser.parseMods.interfaces.IDataSet;

import java.util.HashMap;
import java.util.Map;

import static ru.naumen.sd40.log.parser.NumberUtils.getSafeDouble;
import static ru.naumen.sd40.log.parser.NumberUtils.roundToTwoPlaces;
import static ru.naumen.sd40.log.parser.parseMods.top.TopParseMode.Top.*;

public class TopDataSet implements IDataSet {
    private DescriptiveStatistics laStat = new DescriptiveStatistics();
    private DescriptiveStatistics cpuStat = new DescriptiveStatistics();
    private DescriptiveStatistics memStat = new DescriptiveStatistics();

    public void addLa(double la)
    {
        laStat.addValue(la);
    }

    public void addCpu(double cpu)
    {
        cpuStat.addValue(cpu);
    }

    public void addMem(double mem)
    {
        memStat.addValue(mem);
    }

    public boolean isNaN()
    {
        return laStat.getN() == 0 && cpuStat.getN() == 0 && memStat.getN() == 0;
    }

    public double getAvgLa()
    {
        return roundToTwoPlaces(getSafeDouble(laStat.getMean()));
    }

    public double getAvgCpuUsage()
    {
        return roundToTwoPlaces(getSafeDouble(cpuStat.getMean()));
    }

    public double getAvgMemUsage()
    {
        return roundToTwoPlaces(getSafeDouble(memStat.getMean()));
    }

    public double getMaxLa()
    {
        return roundToTwoPlaces(getSafeDouble(laStat.getMax()));
    }

    public double getMaxCpu()
    {
        return roundToTwoPlaces(getSafeDouble(cpuStat.getMax()));
    }

    public double getMaxMem()
    {
        return roundToTwoPlaces(getSafeDouble(memStat.getMax()));
    }

    @Override
    public Map<String, Object> getRecords() {
        Map<String, Object> records = new HashMap<>();

        if (!isNaN()) {
            records.put(AVG_LA, getAvgLa());
            records.put(AVG_CPU, getAvgCpuUsage());
            records.put(AVG_MEM, getAvgMemUsage());
            records.put(MAX_LA, getMaxLa());
            records.put(MAX_CPU, getMaxCpu());
            records.put(MAX_MEM, getMaxMem());
        }

        return records;
    }
}
