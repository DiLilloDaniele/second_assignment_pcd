package simulator.task;

import simulator.model.Body;
import simulator.model.V2d;
import simulator.strategy.ComputeTotalForce;

public class UpdateVelocityTask implements Runnable {

    private Body body;
    private ComputeTotalForce computeTotalForceStrategy;
    private double dt;

    public UpdateVelocityTask(final Body body, final ComputeTotalForce computeTotalForceStrategy, final double dt) {
        this.body = body;
        this.computeTotalForceStrategy = computeTotalForceStrategy;
        this.dt = dt;
    }

    @Override
    public void run() {
        /* compute total force on bodies */
        V2d totalForce = this.computeTotalForceStrategy.computeTotalForceOnBody(body);

        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());

        /* update velocity */
        body.updateVelocity(acc, dt);
    }

}
