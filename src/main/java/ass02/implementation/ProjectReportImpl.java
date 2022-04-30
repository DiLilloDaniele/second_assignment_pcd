package ass02.implementation;

import ass02.ClassReport;
import ass02.InterfaceReport;
import ass02.PackageReport;
import ass02.ProjectReport;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectReportImpl implements ProjectReport {

    private List<ClassReport> classReports;
    private List<PackageReport> packageReports;
    private ClassReport mainClass;

    @Override
    public ClassReport getMainClass() {
        return mainClass;
    }

    @Override
    public void setMainClass(ClassReport classReport) {
        this.mainClass = classReport;
    }

    @Override
    public List<ClassReport> getAllClasses() {
        return classReports;
    }

    @Override
    public void addClassReport(ClassReport classReport) {
        this.classReports.add(classReport);
    }

    @Override
    public List<PackageReport> getAllPackages() {
        return packageReports;
    }

    @Override
    public void addPackageReport(PackageReport packageReport) {
        this.packageReports.add(packageReport);
    }

    @Override
    public ClassReport getClassReport(String fullClassName) {
        //TODO gestire errore
        return classReports.stream().filter(i -> i.getFullClassName() == fullClassName).collect(Collectors.toList()).get(0);
    }
}
