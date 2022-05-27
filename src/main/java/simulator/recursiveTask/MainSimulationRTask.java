package simulator.recursiveTask;

import simulator.SimulationView;
import simulator.model.Body;
import simulator.model.Boundary;
import simulator.strategy.ComputeTotalForce;
import simulator.task.CheckCollisionTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MainSimulationRTask extends RecursiveAction {

    protected ArrayList<Body> bodies;
    protected ComputeTotalForce computeTotalForceStrategy;
    protected double dt;
    protected Boundary bounds;

    public MainSimulationRTask(final ArrayList<Body> bodies, final ComputeTotalForce computeTotalForceStrategy,
                               final double dt, final Boundary bounds) {
        this.bodies = bodies;
        this.computeTotalForceStrategy = computeTotalForceStrategy;
        this.dt = dt;
        this.bounds = bounds;
    }

    @Override
    protected void compute() {
        UpdateVelocityRTask velocityRTask = new UpdateVelocityRTask(bodies, computeTotalForceStrategy, dt);
        velocityRTask.fork();
        velocityRTask.join();

        UpdatePositionRTask positionRTask = new UpdatePositionRTask(bodies, dt, bounds);
        positionRTask.fork();
        positionRTask.join();

        CheckCollisionRTask collisionRTask = new CheckCollisionRTask(bodies, dt, bounds);
        collisionRTask.fork();
        collisionRTask.join();

    }

}
