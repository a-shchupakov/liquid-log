package ru.naumen.sd40.log.parser.storages;

public class ErrorDataStorage implements IDataStorage {
    private long warnCount;
    private long errorCount;
    private long fatalCount;

    public long getWarnCount()
    {
        return warnCount;
    }
    public void increaseWarnCount() {
        this.warnCount++;
    }

    public long getErrorCount()
    {
        return errorCount;
    }
    public void increaseErrorCount() {
        this.errorCount++;
    }


    public long getFatalCount()
    {
        return fatalCount;
    }
    public void increaseFatalCount() {
        this.fatalCount++;
    }
}
