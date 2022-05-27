package simulator.recursiveTask;

import simulator.model.Body;
import simulator.model.V2d;
import simulator.strategy.ComputeTotalForce;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class UpdateVelocityRTask extends AbstractRecursiveAction {

    private ComputeTotalForce computeTotalForceStrategy;

    public UpdateVelocityRTask(final List<Body> bodies, final ComputeTotalForce computeTotalForceStrategy, final double dt) {
        this.bodies = bodies;
        this.computeTotalForceStrategy = computeTotalForceStrategy;
        this.dt = dt;
    }

    private void computeSingle() {
        /* compute total force on bodies */
        V2d totalForce = this.computeTotalForceStrategy.computeTotalForceOnBody(bodies.get(0));

        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / bodies.get(0).getMass());

        /* update velocity */
        bodies.get(0).updateVelocity(acc, dt);


    }

    public void computeDirectly() {
        if(bodies.size() == MIN_THRESHOLD) {
            computeSingle();
            return;
        }
        for(Body body : bodies) {
            /* compute total force on bodies */
            V2d totalForce = this.computeTotalForceStrategy.computeTotalForceOnBody(body);

            /* compute instant acceleration */
            V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());

            /* update velocity */
            body.updateVelocity(acc, dt);
        }

    }

    @Override
    public void compute() {
        int size = bodies.size();
        if(size <= THRESHOLD) {
            computeDirectly();
            return;
        }
        int split = size / 2;

        invokeAll(new UpdateVelocityRTask(bodies.subList(0, split), computeTotalForceStrategy, dt),
                new UpdateVelocityRTask(bodies.subList(split, size), computeTotalForceStrategy, dt));
    }

}
