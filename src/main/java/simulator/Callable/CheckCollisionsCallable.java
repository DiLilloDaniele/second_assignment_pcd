package simulator.Callable;

import simulator.model.Body;
import simulator.model.Boundary;

import java.util.concurrent.Callable;

public class CheckCollisionsCallable implements Callable<Integer> {

    private Body body;
    private double dt;
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
