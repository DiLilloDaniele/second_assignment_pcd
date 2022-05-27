package simulator.recursiveTask;

import simulator.model.Body;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public abstract class AbstractRecursiveAction extends RecursiveAction {
    protected static final int THRESHOLD = 1;
    protected static final int MIN_THRESHOLD = 1;
    protected List<Body> bodies;
    protected double dt;
}
