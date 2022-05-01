package ass02.verticle;

import ass02.ProjectAnalyzer;
import ass02.implementation.ProjectAnalyzerImpl;
import io.vertx.core.AbstractVerticle;

public class ViewVerticle extends AbstractVerticle {

    public void start() {

    }

    public void stop() {
        System.out.println("Undeploy");
    }

}
