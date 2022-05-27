package simulator.Callable;

import simulator.model.Body;
import simulator.model.Boundary;

import java.util.concurrent.Callable;

public class CheckCollisionsCallable extends AbstractCallable implements Callable<Integer> {

    private Boundary bounds;

    public CheckCollisionsCallable(final Body body, final double dt, final Boundary bounds) {
        this.body = body;
        this.dt = dt;
        this.bounds = bounds;
    }

    @Override
    public Integer call() throws Exception {
        body.checkAndSolveBoundaryCollision(bounds);
        return 0;
    }
}
