package ass02.implementation;

import ass02.*;
import ass02.utility.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReactiveAnalyzerImpl implements ReactiveAnalyzer {

    private Vertx vertx;

    public ReactiveAnalyzerImpl() {
        this.vertx = Vertx.vertx();
    }

    @Override
    public Flowable<InterfaceReport> getInterfaceReport(String srcClassPath) {
        return Flowable.fromCallable(() -> {
            InterfaceReport report = new InterfaceReportImpl();
            try {
                //usare vertx per i file?
                File myFile = new File(srcClassPath);
                report.setFullPath(myFile.getAbsolutePath());
                CompilationUnit cu = StaticJavaParser.parse(myFile);

                InterfacesCollector interfacesCollector = new InterfacesCollector();
                interfacesCollector.visit(cu, report);
            } catch (Exception e) {
                e.printStackTrace();
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
                        e.printStackTrace();
                    }

                    return report;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single());
    }

    @Override
    public Flowable<PackageReport> getPackageReport(String srcPackagePath) {
        return null;
    }

    @Override
    public Flowable<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        return null;
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> source) {

        Flowable.just(srcProjectFolderName)
                .subscribeOn(Schedulers.io())
                .flatMap(i -> Flowable.fromIterable(Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderName).listFiles())).peek((file) -> {
                    //System.out.println("AHO: " + Thread.currentThread().getName());
                }).collect(Collectors.toList())))
                .doOnNext(file -> {
                    //System.out.println("doOnNext() - " + Thread.currentThread().getName() + ": " + file.getName());
                })
                .flatMap(i -> Flowable.just(i)
                        .doOnNext((file) -> {
                            //ESEGUO L'ANALISI DEL FILE
                            System.out.println(Thread.currentThread().getName() + ": " + file.getName());
                            if(file.isFile()) {
                                CompilationUnit cu = StaticJavaParser.parse(file);
                                VisitorReactive visitorWithCallback = new VisitorReactive();
                                visitorWithCallback.visit(cu,source);
                            } else {
                                //è un package
                                ProjectElem projectElem = new ProjectElemImpl(file.getName(), ProjectElemImpl.Type.Package);
                                source.onNext(projectElem);
                                analyzeProject(file.getPath(), source);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                ).subscribe(i -> {
                    //aggiornamento gui
                    //ProjectElem elem = new ProjectElemImpl(i.getName(), ProjectElemImpl.Type.None);
                    //callback.accept(elem);
                    //System.out.println(Thread.currentThread().getName() + " - Finito");
                });


    }

}
