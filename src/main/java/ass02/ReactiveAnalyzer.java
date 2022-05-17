package ass02;

import ass02.implementation.InterfaceReportImpl;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.function.Consumer;

public interface ReactiveAnalyzer {

    Flowable<InterfaceReport> getInterfaceReport(String srcClassPath);

    Flowable<ClassReport> getClassReport(String srcClassPath);

    Flowable<PackageReport> getPackageReport(String srcPackagePath);

    Flowable<ProjectReport> getProjectReport(String srcProjectFolderPath);

    void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> source);

}
