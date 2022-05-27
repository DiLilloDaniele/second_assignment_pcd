package ass02.verticle;

import ass02.ProjectAnalyzer;
import ass02.ProjectElem;
import ass02.implementation.ProjectAnalyzerImpl;
import ass02.implementation.ProjectElemImpl;
import ass02.implementation.ProjectElemImpl.Type;
import ass02.passiveComponents.CountersMonitor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import javax.swing.*;
import java.util.function.Consumer;

public class AnalyzerVerticle extends AbstractVerticle {

    private Consumer<ProjectElem> callback;
    private String srcProjectFolderName;
    private CountersMonitor monitor;

    public AnalyzerVerticle(final String srcProjectFolderName, final Consumer<ProjectElem> callback, final CountersMonitor monitor) {
        super();
        this.srcProjectFolderName = srcProjectFolderName;
        this.callback = callback;
        this.monitor = monitor;
    }

    public void start() {

        ProjectAnalyzer analyzer = new ProjectAnalyzerImpl(this.getVertx(), monitor);
        analyzer.analyzeProject(srcProjectFolderName, "update-gui");

    }

    public void stop() throws Exception {
        Future<Void> future = vertx.undeploy(deploymentID());
    }

}
