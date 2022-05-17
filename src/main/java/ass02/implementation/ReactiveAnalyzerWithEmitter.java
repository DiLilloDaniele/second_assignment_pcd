package ass02.implementation;

import ass02.ProjectElem;
import ass02.utility.VisitorReactive;
import ass02.utility.VisitorWithCallback;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReactiveAnalyzerWithEmitter extends ReactiveAnalyzerImpl {

    private Disposable disposable;

    @Override
    public void analyzeProject(String srcProjectFolderName, PublishSubject<ProjectElem> pubsub) {
        Observable<File> source = Observable.create(emitter -> {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": INIZIO IL PARSING");
                File[] filesArray = new File(srcProjectFolderName).listFiles();
                List<File> files = Arrays.stream(filesArray).collect(Collectors.toList());
                for (File f : files) {
                    emitter.onNext(f);
                }
            }).start();
        });

        this.disposable = source.observeOn(Schedulers.computation()).subscribe((file) -> {
            System.out.println(Thread.currentThread().getName() + ": " + file.getName());
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
                analyzeProject(file.getPath(), pubsub);
            }
        });

    }

    public void stop() {
        this.disposable.dispose();
    }

}
