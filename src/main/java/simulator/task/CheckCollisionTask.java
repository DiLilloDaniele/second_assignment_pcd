package simulator.task;

import simulator.model.Body;
import simulator.model.Boundary;

public class CheckCollisionTask implements Runnable{

    private Body body;
    private Boundary bounds;

    public CheckCollisionTask(final Body body, final Boundary bounds) {
        this.body = body;
        this.bounds = bounds;
    }

    @Override
    public void run() {
        body.checkAndSolveBoundaryCollision(bounds);
    }
}
