package simulator.Callable;

import simulator.model.Body;
import simulator.model.V2d;
import simulator.strategy.ComputeTotalForce;

import java.util.concurrent.Callable;

public class UpdateVelocityCallable extends AbstractCallable implements Callable<Integer> {

    private ComputeTotalForce computeTotalForceStrategy;

    public UpdateVelocityCallable(final Body body, final ComputeTotalForce computeTotalForceStrategy, final double dt) {
        this.body = body;
        this.computeTotalForceStrategy = computeTotalForceStrategy;
        this.dt = dt;
    }

    @Override
    public Integer call() throws Exception {
        /* compute total force on bodies */
        V2d totalForce = this.computeTotalForceStrategy.computeTotalForceOnBody(body);

        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());

        /* update velocity */
        body.updateVelocity(acc, dt);

        return 0;
    }
}
