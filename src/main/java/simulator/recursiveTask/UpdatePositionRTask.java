package simulator.recursiveTask;

import simulator.model.Body;
import simulator.model.Boundary;
import simulator.model.V2d;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class UpdatePositionRTask extends AbstractRecursiveAction {

    private Boundary bounds;

    public UpdatePositionRTask(final List<Body> bodies, final double dt, final Boundary bounds) {
        this.bodies = bodies;
        this.dt = dt;
        this.bounds = bounds;
    }

    public void computeDirectly() {
        if(bodies.size() == MIN_THRESHOLD) {
            bodies.get(0).updatePos(dt);
            bodies.get(0).checkAndSolveBoundaryCollision(bounds);
            return;
        }
        for(Body body : bodies) {
            body.updatePos(dt);
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

        invokeAll(new UpdatePositionRTask(bodies.subList(0, split), dt, bounds),
                new UpdatePositionRTask(bodies.subList(split, size), dt, bounds));
    }
}
