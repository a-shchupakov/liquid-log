package ru.naumen.sd40.log.parser.storages;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionDataStorage implements IDataStorage {
    ArrayList<Integer> times = new ArrayList<>();
    double min;
    double mean;
    double stddev;
    double percent50;
    double percent95;
    double percent99;

    double percent999;
    double max;
    long count;
    private int addObjectActions = 0;
    private int editObjectsActions = 0;
    private int getListActions = 0;
    private int commentActions = 0;
    private int getCatalogsActions = 0;

    private int getFormActions = 0;

    private int getDtObjectActions = 0;

    private int searchActions = 0;

    boolean isNaN = true;

    private HashMap<String, Integer> actions = new HashMap<>();

    public void calculate()
    {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        times.forEach(t -> ds.addValue(t));
        min = ds.getMin();
        mean = ds.getMean();
        stddev = ds.getStandardDeviation();
        percent50 = ds.getPercentile(50.0);
        percent95 = ds.getPercentile(95.0);
        percent99 = ds.getPercentile(99.0);
        percent999 = ds.getPercentile(99.9);
        max = ds.getMax();
        count = ds.getN();
        isNaN = count == 0;
    }

    public HashMap<String, Integer> getActionsCounter()
    {
        return actions;
    }

    public int getListActions()
    {
        return getListActions;
    }
    public void increaseListActions() {
        getListActions++;
    }

    public int getAddObjectActions()
    {
        return addObjectActions;
    }
    public void increaseAddObjectActions() {
        addObjectActions++;
    }

    public int getCommentActions()
    {
        return commentActions;
    }
    public void increaseCommentActions() {
        commentActions++;
    }

    public long getCount()
    {
        return count;
    }
    public void increasetCount() {
        count++;
    }

    public int getDtObjectActions()
    {
        return getDtObjectActions;
    }
    public void increaseDtObjectActions() {
        getDtObjectActions++;
    }

    public int getEditObjectsActions()
    {
        return editObjectsActions;
    }
    public void increaseEditObjectsActions() {
        editObjectsActions++;
    }

    public int getFormActions()
    {
        return getFormActions;
    }
    public void increaseFormActions() {
        getFormActions++;
    }

    public int getCatalogsActions()
    {
        return getCatalogsActions;
    }
    public void increaseCatalogsActions() {
        getCatalogsActions++;
    }

    public double getMax()
    {
        return max;
    }

    public double getMean()
    {
        return mean;
    }

    public double getMin()
    {
        return min;
    }

    public double getPercent50()
    {
        return percent50;
    }

    public double getPercent95()
    {
        return percent95;
    }

    public double getPercent99()
    {
        return percent99;
    }

    public double getPercent999()
    {
        return percent999;
    }

    public int getSearchActions()
    {
        return searchActions;
    }
    public void increaseSearchActions(){
        searchActions++;
    }

    public double getStddev()
    {
        return stddev;
    }

    public ArrayList<Integer> getTimes()
    {
        return times;
    }

    public boolean isNaN()
    {
        return isNaN;
    }
}
