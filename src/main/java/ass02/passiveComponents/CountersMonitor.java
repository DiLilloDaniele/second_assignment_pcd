package ass02.passiveComponents;

import java.util.concurrent.atomic.AtomicBoolean;

public class CountersMonitor {

    public AtomicBoolean closed = new AtomicBoolean();
    private int numClasses = 0;
    private int numFields = 0;
    private int numPackages = 0;
    private int numInterfaces = 0;
    private int numEnums = 0;
    private int numMethods = 0;

    public CountersMonitor() {
        this.closed.set(false);
    }

    public void close() {
        this.closed.set(true);
    }

    public int getNumClasses() {
        return numClasses;
    }

    public int getNumFields() {
        return numFields;
    }

    public int getNumPackages() {
        return numPackages;
    }

    public int getNumInterfaces() {
        return numInterfaces;
    }

    public int getNumEnums() {
        return numEnums;
    }

    public int getNumMethods() {
        return numMethods;
    }

    public void incClasses() {
        this.numClasses++;
    }

    public void incFields() {
        this.numFields++;
    }

    public void incPackages() {
        this.numPackages++;
    }

    public void incInterfaces() {
        this.numInterfaces++;
    }

    public void incEnums() {
        this.numEnums++;
    }

    public void incMethods() {
        this.numMethods++;
    }

}
