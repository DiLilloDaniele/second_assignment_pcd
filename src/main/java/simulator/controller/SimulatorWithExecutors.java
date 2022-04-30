package simulator.controller;

import simulator.Callable.CheckCollisionsCallable;
import simulator.Callable.UpdatePositionCallable;
import simulator.Callable.UpdateVelocityCallable;
import simulator.SimulationView;
import simulator.model.Body;
import simulator.model.Boundary;
import simulator.model.P2d;
import simulator.model.V2d;
import simulator.strategy.ComputeTotalForce;
import simulator.task.CheckCollisionTask;
import simulator.task.UpdatePositionTask;
import simulator.task.UpdateVelocityTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class SimulatorWithExecutors {

    private SimulationView viewer = null;

    /* bodies in the field */
    ArrayList<Body> bodies;

    private ExecutorService executor;

    /* boundary of the field */
    private Boundary bounds;

    /* virtual time */
    private double vt;

    /* virtual time step */
    double dt;

    ComputeTotalForce computeTotalForceStrategy;

    public SimulatorWithExecutors() {
        init();
    }

    public SimulatorWithExecutors(SimulationView viewer) {
        this.viewer = viewer;
        init();
    }

    private void init() {
        this.createBodies();
        this.computeTotalForceStrategy = (Body b) -> {
            V2d totalForce = new V2d(0, 0);
            for (int j = 0; j < bodies.size(); j++) {
                Body otherBody = bodies.get(j);
                if (!b.equals(otherBody)) {
                    try {
                        V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
                        totalForce.sum(forceByOtherBody);
                    } catch (Exception ex) {
                    }
                }
            }

            /* add friction force */
            totalForce.sum(b.getCurrentFrictionForce());

            return totalForce;
        };
    }

    public void execute(long nSteps) throws InterruptedException {
        vt = 0;
        dt = 0.001;
        long iter = 0;

        while (iter < nSteps) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            bodies.stream().forEach(b -> {
                executor.execute(new UpdateVelocityTask(b, computeTotalForceStrategy, dt));
            });
            executor.shutdown();
            //idea: usare le future invece di terminare l'executor e ricrearlo
            executor.awaitTermination(60, TimeUnit.SECONDS);
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            bodies.stream().forEach(b -> {
                executor.execute(new UpdatePositionTask(b, dt, bounds));
            });
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);

            //System.out.println("FINITA ITERAZIONE");
            vt = vt + dt;
            iter++;
        }

    }

    public void executeWithFutures(long nSteps) throws InterruptedException, ExecutionException {
        vt = 0;
        dt = 0.001;
        List<Future<Integer>> futures = new ArrayList<>();
        long iter = 0;
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        while (iter < nSteps) {
            futures = new ArrayList<>();
            for (Body b : bodies) {
                Future<Integer> future = executor.submit(new UpdateVelocityCallable(b, computeTotalForceStrategy, dt));
                futures.add(future);
            }
            for (Future<Integer> future : futures) {
                future.get();
                //futures.remove(future);
            }
            futures = new ArrayList<>();
            for (Body b : bodies) {
                Future<Integer> future = executor.submit(new UpdatePositionCallable(b, dt, bounds));
                futures.add(future);
            }
            for (Future<Integer> future : futures) {
                future.get();
                //futures.remove(future);
            }
            futures = new ArrayList<>();
            for (Body b : bodies) {
                Future<Integer> future = executor.submit(new CheckCollisionsCallable(b, dt, bounds));
                futures.add(future);
            }
            for (Future<Integer> future : futures) {
                future.get();
                //futures.remove(future);
            }
            vt = vt + dt;
            iter++;
            if(viewer != null)
                viewer.display(bodies, vt, iter, bounds);
        }
        executor.shutdown();
    }

    private void createBodies() {
        bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        int nBodies = 1000;
        Random rand = new Random(System.currentTimeMillis());
        bodies = new ArrayList<Body>();
        for (int i = 0; i < nBodies; i++) {
            double x = bounds.getX0()*0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
            double y = bounds.getY0()*0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
            Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
            bodies.add(b);
        }
    }
}
