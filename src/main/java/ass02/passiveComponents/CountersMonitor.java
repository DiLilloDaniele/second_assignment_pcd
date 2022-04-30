package ass02.passiveComponents;

public class CountersMonitor {

    private int numClasses = 0;
    private int numFields = 0;
    private int numPackages = 0;
    private int numInterfaces = 0;
    private int numEnums = 0;
    private int numMethods = 0;

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

    public synchronized void incClasses() {
        this.numClasses++;
    }

    public synchronized void incFields() {
        this.numFields++;
    }

    public synchronized void incPackages() {
        this.numPackages++;
    }

    public synchronized void incInterfaces() {
        this.numInterfaces++;
    }

    public synchronized void incEnums() {
        this.numEnums++;
    }

    public synchronized void incMethods() {
        this.numMethods++;
    }

}
