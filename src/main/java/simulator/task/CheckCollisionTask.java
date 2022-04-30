package simulator.task;

import simulator.model.Body;
import simulator.model.Boundary;

public class CheckCollisionTask implements Runnable{

    private Body body;
    private double dt;
    private Boundary bounds;

    public CheckCollisionTask(final Body body, final double dt, final Boundary bounds) {
        this.body = body;
        this.dt = dt;
        this.bounds = bounds;
    }

    @Override
    public void run() {
        body.checkAndSolveBoundaryCollision(bounds);
    }
}
