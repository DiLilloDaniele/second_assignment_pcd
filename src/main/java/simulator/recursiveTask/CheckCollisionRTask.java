package simulator.recursiveTask;

import simulator.model.Body;
import simulator.model.Boundary;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class CheckCollisionRTask extends AbstractRecursiveAction {

    private Boundary bounds;

    public CheckCollisionRTask(final List<Body> bodies, final double dt, final Boundary bounds) {
        this.bodies = bodies;
        this.dt = dt;
        this.bounds = bounds;
    }

    public void computeDirectly() {
        if(bodies.size() == MIN_THRESHOLD) {
            bodies.get(0).checkAndSolveBoundaryCollision(bounds);
        }
        for(Body body : bodies) {
            body.checkAndSolveBoundaryCollision(bounds);
        }

    }

    @Override
    protected void compute() {
        int size = bodies.size();
        if(size <= THRESHOLD) {
            computeDirectly();
            return;
        }
        int split = size / 2;

        invokeAll(new CheckCollisionRTask(bodies.subList(0, split), dt, bounds),
                new CheckCollisionRTask(bodies.subList(split, size), dt, bounds));
    }
}
