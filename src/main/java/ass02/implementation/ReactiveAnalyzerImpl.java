package ass02.implementation;

import ass02.*;
import ass02.passiveComponents.CountersMonitor;
import ass02.utility.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReactiveAnalyzerImpl implements ReactiveAnalyzer {

    private CountersMonitor monitor;

    public ReactiveAnalyzerImpl(CountersMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public Flowable<InterfaceReport> getInterfaceReport(String srcClassPath) {
        return Flowable.fromCallable(() -> {
            InterfaceReport report = new InterfaceReportImpl();
            try {
                File myFile = new File(srcClassPath);
                report.setFullPath(myFile.getAbsolutePath());
                CompilationUnit cu = StaticJavaParser.parse(myFile);

                InterfacesCollector interfacesCollector = new InterfacesCollector();
                interfacesCollector.visit(cu, report);
            } catch (Exception e) {
                //silently ignored
            }

            return report;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.single());
    }

    @Override
    public Flowable<ClassReport> getClassReport(String srcClassPath) {
        return Flowable.fromCallable(() -> {
                    ClassReport report = new ClassReportImpl();
                    try {
                        File myFile = new File(srcClassPath);
                        report.setFullPath(myFile.getAbsolutePath());
                        CompilationUnit cu = StaticJavaParser.parse(myFile);

                        ClassCollector interfacesCollector = new ClassCollector();
                        interfacesCollector.visit(cu, report);
                    } catch (Exception e) {
                        //silently ignored
                    }

                    return report;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
    }

    @Override
    public Flowable<PackageReport> getPackageReport(String srcPackagePath) {
        return Flowable.fromCallable(() -> {
                    PackageReport report = new PackageReportImpl();
                    try {
                        File folder = new File(srcPackagePath);
                        report.setFullNamePackage(folder.getName());
                        File[] listOfFiles = folder.listFiles();
                        for (File file : listOfFiles) {
                            try {
                                if(file.isFile()) {

                                    CompilationUnit cu = StaticJavaParser.parse(file);
                                    FileTypeImpl fileType = new FileTypeImpl();
                                    ClassOrInterfaceChecker classOrInterfaceChecker = new ClassOrInterfaceChecker();
                                    classOrInterfaceChecker.visit(cu,fileType);
                                    if(fileType.isInterface()) {
                                        this.getInterfaceReport(file.getAbsolutePath())
                                                .blockingSubscribe(i -> {
                                                    report.addInterfaceReport(i);
                                                });
                                    } else {
                                        this.getClassReport(file.getAbsolutePath())
                                                .blockingSubscribe(i -> {
                                                    report.addClassReport(i);
                                                });
                                    }
                                } else {

                                    this.getPackageReport(file.getAbsolutePath())
                                            .blockingSubscribe(i -> {
                                                report.addPackageReport(i);
                                            });
                                }
                            } catch (FileNotFoundException e) {
                                //silently ignored
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        //silently ignored
                        e.printStackTrace();
                    }
                    return report;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
    }

    @Override
    public Flowable<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        return Flowable.fromCallable(() -> {
                    ProjectReport report = new ProjectReportImpl();
                    try {
                        File folder = new File(srcProjectFolderPath);
                        File[] listOfFiles = folder.listFiles();
                        for (File file : listOfFiles) {
                            try {
                                if(file.isFile()) {

                                    CompilationUnit cu = StaticJavaParser.parse(file);
                                    FileTypeImpl fileType = new FileTypeImpl();
                                    ClassOrInterfaceChecker classOrInterfaceChecker = new ClassOrInterfaceChecker();
                                    classOrInterfaceChecker.visit(cu,fileType);
                                    if(!fileType.isInterface()) {
                                        this.getClassReport(file.getAbsolutePath())
                                                .blockingSubscribe(i -> {
                                                    if(fileType.isMainClass()) {
                                                        report.setMainClass(i);
                                                    }
                                                    report.addClassReport(i);
                                                });
                                    }
                                } else {
                                    this.getPackageReport(file.getAbsolutePath())
                                            .blockingSubscribe(i -> {
                                                report.addPackageReport(i);
                                            });
                                }
                            } catch (FileNotFoundException e) {
                                //silently ignored
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        //silently ignored
                    }
                    return report;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> source) {
        Flowable.just(srcProjectFolderName)
                .subscribeOn(Schedulers.io())
                .flatMap(i -> Flowable.fromIterable(Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderName).listFiles())).collect(Collectors.toList())))
                .flatMap(i -> Flowable.just(i)
                        .doOnNext((file) -> {
                            if(monitor.closed.get()) {
                                if(file.isFile()) {
                                    CompilationUnit cu = StaticJavaParser.parse(file);
                                    VisitorReactive visitorWithCallback = new VisitorReactive();
                                    visitorWithCallback.visit(cu,source);
                                } else {
                                    ProjectElem projectElem = new ProjectElemImpl(file.getName(), ProjectElemImpl.Type.Package);
                                    source.onNext(projectElem);
                                    analyzeProject(file.getPath(), source);
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                ).subscribe(i -> {
                });
    }

    @Override
    public void stop() {
        monitor.close();
    }

}
