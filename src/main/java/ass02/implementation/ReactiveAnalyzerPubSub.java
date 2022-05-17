package ass02.implementation;

import ass02.ProjectElem;
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

    @Override
    public void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> pubsub) {

        PublishSubject<String> sourceFiles = PublishSubject.<String>create();
        PublishSubject<File> sourceComputation = PublishSubject.<File>create();
        sourceComputation.subscribe(file -> {
            if(file.isFile()) {
                try {
                    CompilationUnit cu = StaticJavaParser.parse(file);
                    VisitorReactive visitor = new VisitorReactive();
                    visitor.visit(cu, pubsub);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                //è un package
                ProjectElem projectElem = new ProjectElemImpl(file.getName(), ProjectElemImpl.Type.Package);
                pubsub.onNext(projectElem);
                sourceFiles.onNext(file.getPath());
            }
        });

        sourceFiles.subscribe(path -> {
            File[] filesArray = new File(path).listFiles();
            List<File> files = Arrays.stream(filesArray).collect(Collectors.toList());
            for (File f : files) {
                sourceComputation.onNext(f);
            }
        });
        sourceFiles.onNext(srcProjectFolderName);
    }

    public void stop() {
        this.disposable.dispose();
    }

}
