package ass02.implementation;

import ass02.*;
import ass02.passiveComponents.CountersMonitor;
import ass02.utility.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.vertx.core.*;
import io.vertx.core.file.FileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

    Vertx vertx;
    CountersMonitor monitor;

    public ProjectAnalyzerImpl(final Vertx vertx, final CountersMonitor countersMonitor) {
        this.vertx = vertx;
        this.monitor = countersMonitor;
    }

    @Override
    public Future<InterfaceReport> getInterfaceReport(String srcClassPath) {

        Future<InterfaceReport> fut = vertx.executeBlocking((i) -> {
            try {
                InterfaceReportImpl report = new InterfaceReportImpl();
                File myFile = new File(srcClassPath);
                report.setFullPath(myFile.getAbsolutePath());
                CompilationUnit cu = StaticJavaParser.parse(myFile);

                InterfacesCollector interfacesCollector = new InterfacesCollector();
                interfacesCollector.visit(cu, report);
                i.complete(report);

            } catch (Exception e) {
                e.printStackTrace();
                i.fail("ERRORE");
            }
        }, false);
        return fut;
    }

    @Override
    public Future<ClassReport> getClassReport(String srcClassPath) {
        Future<ClassReport> fut = vertx.executeBlocking((i) -> {
            try {
                ClassReportImpl report = new ClassReportImpl();
                File myFile = new File(srcClassPath);
                report.setFullPath(myFile.getAbsolutePath());
                CompilationUnit cu = StaticJavaParser.parse(myFile);
                ClassCollector classCollector = new ClassCollector();
                classCollector.visit(cu, report);
                i.complete(report);

            } catch (Exception e) {
                e.printStackTrace();
                i.fail("ERRORE");
            }
        }, false);
        return fut;
    }

    @Override
    public Future<PackageReport> getPackageReport(String srcPackagePath) {
        Future<PackageReport> future = vertx.executeBlocking((i) -> {
            List<Future> futures = new ArrayList<>();
            File folder = new File(srcPackagePath);
            File[] listOfFiles = folder.listFiles();
            PackageReportImpl packageReport = new PackageReportImpl();
            packageReport.setFullNamePackage(folder.getName());
            for (File file : listOfFiles) {
                try {
                    if(file.isFile()) {
                        CompilationUnit cu = StaticJavaParser.parse(file);
                        FileTypeImpl fileType = new FileTypeImpl();
                        ClassOrInterfaceChecker classOrInterfaceChecker = new ClassOrInterfaceChecker();
                        classOrInterfaceChecker.visit(cu,fileType);
                        if(fileType.isInterface()) {
                            Future<InterfaceReport> fut = this.getInterfaceReport(file.getAbsolutePath());
                            futures.add(fut);
                            fut.onComplete((AsyncResult<InterfaceReport> res) -> {
                                if(res != null) {
                                    packageReport.addInterfaceReport(res.result());
                                }
                            });

                        } else {
                            Future<ClassReport> fut = this.getClassReport(file.getAbsolutePath());
                            futures.add(fut);
                            fut.onComplete((AsyncResult<ClassReport> res) -> {
                                if(res != null) {
                                    packageReport.addClassReport(res.result());
                                }
                            });
                        }
                    } else {
                        Future<PackageReport> fut = getPackageReport(file.getPath());
                        futures.add(fut);
                        fut.onComplete((AsyncResult<PackageReport> res) -> {
                            if(res != null) {
                                packageReport.addPackageReport(res.result());
                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    i.fail("ERRORE");
                }
            }
            CompositeFuture
            .all(futures)
            .onSuccess((CompositeFuture res) -> {
                i.complete(packageReport);
            });
        }, false);

        return future;
    }

    @Override
    public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        Future<ProjectReport> future = vertx.executeBlocking((i) -> {
            List<Future> futures = new ArrayList<>();
            File folder = new File(srcProjectFolderPath);
            File[] listOfFiles = folder.listFiles();
            ProjectReport projectReport = new ProjectReportImpl();
            for (File file : listOfFiles) {
                try {
                    if(file.isFile()) {
                        CompilationUnit cu = StaticJavaParser.parse(file);
                        FileTypeImpl fileType = new FileTypeImpl();
                        ClassOrInterfaceChecker classOrInterfaceChecker = new ClassOrInterfaceChecker();
                        classOrInterfaceChecker.visit(cu,fileType);
                        if(!fileType.isInterface()) {
                            Future<ClassReport> fut = this.getClassReport(file.getAbsolutePath());
                            futures.add(fut);
                            fut.onComplete((AsyncResult<ClassReport> res) -> {
                                if(res != null) {
                                    if(fileType.isMainClass()) {
                                        projectReport.setMainClass(res.result());
                                    }
                                    projectReport.addClassReport(res.result());
                                }
                            });
                        }
                    } else {
                        Future<PackageReport> fut = getPackageReport(file.getPath());
                        futures.add(fut);
                        fut.onComplete((AsyncResult<PackageReport> res) -> {
                            if(res != null) {
                                projectReport.addPackageReport(res.result());
                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    i.fail("ERRORE");
                }
            }
            CompositeFuture
                .all(futures)
                .onSuccess((CompositeFuture res) -> {
                    i.complete(projectReport);
                });
        }, false);

        return future;
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, Consumer<ProjectElem> callback) {
        vertx.executeBlocking((s) -> {
            File folder = new File(srcProjectFolderName);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                Future<ProjectReport> future = vertx.executeBlocking((i) -> {
                    try {
                        if(file.isFile()) {
                            CompilationUnit cu = StaticJavaParser.parse(file);
                            VisitorWithCallback visitorWithCallback = new VisitorWithCallback();
                            visitorWithCallback.visit(cu,callback);
                        } else {
                            ProjectElem projectElem = new ProjectElemImpl(file.getName(), ProjectElemImpl.Type.Package);
                            callback.accept(projectElem);
                            analyzeProject(file.getPath(), callback);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }, false);

            }
        }, false);

    }


    public void analyzeProject(String srcProjectFolderName, String topic) {
        vertx.executeBlocking((s) -> {
            File folder = new File(srcProjectFolderName);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if(!monitor.closed.get()) {
                    Future<ProjectReport> future = vertx.executeBlocking((i) -> {
                        try {
                            if(file.isFile()) {
                                CompilationUnit cu = StaticJavaParser.parse(file);
                                VisitorWithTopic visitorWithTopic = new VisitorWithTopic(this.vertx.eventBus());
                                visitorWithTopic.visit(cu,topic);
                            } else {
                                ProjectElem projectElem = new ProjectElemImpl(file.getName(), ProjectElemImpl.Type.Package);
                                this.vertx.eventBus().publish(topic, "Package-" + projectElem.getNameAsString());
                                analyzeProject(file.getPath(), topic);
                            }
                        } catch (Exception e) {
                            //silently ignored
                        }

                    }, false);
                } else {
                    break;
                }
            }
        }, false);

    }
}
