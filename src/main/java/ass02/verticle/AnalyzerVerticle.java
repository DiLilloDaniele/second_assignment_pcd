package ass02.verticle;

import ass02.ProjectAnalyzer;
import ass02.ProjectElem;
import ass02.implementation.ProjectAnalyzerImpl;
import ass02.implementation.ProjectElemImpl;
import ass02.implementation.ProjectElemImpl.Type;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import javax.swing.*;
import java.util.function.Consumer;

public class AnalyzerVerticle extends AbstractVerticle {

    private Consumer<ProjectElem> callback;
    private String srcProjectFolderName;

    public AnalyzerVerticle(final String srcProjectFolderName, final Consumer<ProjectElem> callback) {
        super();
        this.srcProjectFolderName = srcProjectFolderName;
        this.callback = callback;
    }

    // Called when verticle is deployed
    public void start() {

        ProjectAnalyzer analyzer = new ProjectAnalyzerImpl(this.getVertx());
        analyzer.analyzeProject(srcProjectFolderName, callback);

    }

    // Optional - called when verticle is undeployed
    public void stop() {
        System.out.println("Undeploy");
    }

}
