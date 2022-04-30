package ass02.implementation;

import ass02.ClassReport;
import ass02.InterfaceReport;
import ass02.PackageReport;

import java.util.ArrayList;
import java.util.List;

public class PackageReportImpl implements PackageReport {

    private List<ClassReport> classReports;
    private List<InterfaceReport> interfaceReports;
    private List<PackageReport> packageReports;
    private String fullNamePackage;

    public PackageReportImpl() {
        this.classReports = new ArrayList<>();
        this.interfaceReports = new ArrayList<>();
        this.packageReports = new ArrayList<>();
        this.fullNamePackage = "";
    }

    public PackageReportImpl(List<ClassReport> classReports, List<InterfaceReport> interfaceReports, List<PackageReport> packageReports, String fullNamePackage) {
        this.classReports = classReports;
        this.interfaceReports = interfaceReports;
        this.packageReports = packageReports;
        this.fullNamePackage = fullNamePackage;
    }

    @Override
    public List<ClassReport> getAllClasses() {
        return this.classReports;
    }

    @Override
    public List<InterfaceReport> getAllInterfaces() {
        return this.interfaceReports;
    }

    @Override
    public List<PackageReport> getAllPackages() {
        return this.packageReports;
    }

    @Override
    public String getFullPackageName() {
        return this.fullNamePackage;
    }

    public void addClassReport(ClassReport classReport) {
        this.classReports.add(classReport);
    }

    public void addInterfaceReport(InterfaceReport interfaceReport) {
        this.interfaceReports.add(interfaceReport);
    }

    public void addPackageReport(PackageReport packageReport) {
        this.packageReports.add(packageReport);
    }

    public void setFullNamePackage(String fullNamePackage) {
        this.fullNamePackage = fullNamePackage;
    }
}
