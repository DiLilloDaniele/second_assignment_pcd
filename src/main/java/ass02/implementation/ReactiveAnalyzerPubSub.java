package ass02.implementation;

import ass02.ProjectElem;
import ass02.passiveComponents.CountersMonitor;
import ass02.utility.VisitorReactive;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReactiveAnalyzerPubSub extends ReactiveAnalyzerImpl {

    private Disposable disposable;
    private Disposable fileDisposable;

    public ReactiveAnalyzerPubSub(CountersMonitor monitor) {
        super(monitor);
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> pubsub) {

        PublishSubject<String> sourceFiles = PublishSubject.<String>create();
        PublishSubject<CompilationUnit> sourceComputation = PublishSubject.<CompilationUnit>create();
        disposable = sourceComputation.observeOn(Schedulers.computation()).subscribe(cu -> {
            VisitorReactive visitor = new VisitorReactive();
            visitor.visit(cu, pubsub);
        });

        fileDisposable = sourceFiles.observeOn(Schedulers.io()).subscribe(path -> {
            File[] filesArray = new File(path).listFiles();
            List<File> files = Arrays.stream(filesArray).collect(Collectors.toList());
            for (File f : files) {
                if(f.isFile()) {
                    try {
                    CompilationUnit cu = StaticJavaParser.parse(f);
                    sourceComputation.onNext(cu);
                    } catch (Exception ex) {
                        //silently ignored
                    }
                }
                else {
                    ProjectElem projectElem = new ProjectElemImpl(f.getName(), ProjectElemImpl.Type.Package);
                    pubsub.onNext(projectElem);
                    sourceFiles.onNext(f.getPath());
                }
            }
        });
        sourceFiles.onNext(srcProjectFolderName);
    }

    @Override
    public void stop() {
        this.disposable.dispose();
        this.fileDisposable.dispose();
    }

}
