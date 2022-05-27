package simulator.controller;

import simulator.Callable.CheckCollisionsCallable;
import simulator.Callable.UpdatePositionCallable;
import simulator.Callable.UpdateVelocityCallable;
import simulator.SimulationView;
import simulator.model.Body;
import simulator.model.Boundary;
import simulator.model.P2d;
import simulator.model.V2d;
import simulator.recursiveTask.MainSimulationRTask;
import simulator.strategy.ComputeTotalForce;
import simulator.task.CheckCollisionTask;
import simulator.task.UpdatePositionTask;
import simulator.task.UpdateVelocityTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SimulatorWithExecutors implements Controller{

    public enum ComputationType {
        None,
        Executor,
        ExecutorWithFutures,
        ForkJoin
    }

    private SimulationView viewer = null;
    private ForkJoinPool forkJoinPool = null;
    private ComputationType type;

    /* bodies in the field */
    ArrayList<Body> bodies;

    private ExecutorService executor = null;

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

    public void executeFromGUI(long nSteps, ComputationType type) {
        this.type = type;
        new Thread(() -> {
            try {
                switch (type) {
                    case Executor: this.executeWithExecutors(nSteps); break;
                    case ExecutorWithFutures: this.executeWithFutures(nSteps); break;
                    case ForkJoin: this.executeWithForkJoin(nSteps); break;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void execute(long nSteps, ComputationType type) throws InterruptedException, ExecutionException {
        this.type = type;
        switch (type) {
            case Executor: this.executeWithExecutors(nSteps);System.out.println("h"); break;
            case ExecutorWithFutures: this.executeWithFutures(nSteps);System.out.println("f"); break;
            case ForkJoin: this.executeWithForkJoin(nSteps);System.out.println("r"); break;
        }
    }

    @Override
    public void setView(SimulationView view) {
        this.viewer = view;
    }

    @Override
    public void stopExecution() {
        switch (type) {
            case Executor:
            case ExecutorWithFutures:
                this.executor.shutdown(); break;
            case ForkJoin: this.forkJoinPool.shutdown(); break;
        }
    }

    public void executeWithExecutors(long nSteps) throws InterruptedException {
        vt = 0;
        dt = 0.001;
        long iter = 0;

        while (iter < nSteps) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            bodies.stream().forEach(b -> {
                executor.execute(new UpdateVelocityTask(b, computeTotalForceStrategy, dt));
            });
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            bodies.stream().forEach(b -> {
                executor.execute(new UpdatePositionTask(b, dt, bounds));
            });
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            bodies.stream().forEach(b -> {
                executor.execute(new CheckCollisionTask(b, bounds));
            });
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
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
            try {
                futures = new ArrayList<>();
                for (Body b : bodies) {
                    Future<Integer> future = executor.submit(new UpdateVelocityCallable(b, computeTotalForceStrategy, dt));
                    futures.add(future);
                }
                for (Future<Integer> future : futures) {
                    future.get();
                }
                futures = new ArrayList<>();
                for (Body b : bodies) {
                    Future<Integer> future = executor.submit(new UpdatePositionCallable(b, dt, bounds));
                    futures.add(future);
                }
                for (Future<Integer> future : futures) {
                    future.get();
                }
                futures = new ArrayList<>();
                for (Body b : bodies) {
                    Future<Integer> future = executor.submit(new CheckCollisionsCallable(b, dt, bounds));
                    futures.add(future);
                }
            } catch (RejectedExecutionException e) {
                return;
            }

            for (Future<Integer> future : futures) {
                future.get();
            }
            vt = vt + dt;
            iter++;

            if(viewer != null)
                viewer.display(bodies, vt, iter, bounds);
        }
        executor.shutdown();
    }

    public void executeWithForkJoin(long nSteps) throws InterruptedException {

        forkJoinPool = new ForkJoinPool();
        dt = 0.001;
        vt = 0;
        long iter = 0;
        while (iter < nSteps) {
            try {
                forkJoinPool.invoke(new MainSimulationRTask(bodies, computeTotalForceStrategy, dt, bounds));
            } catch (RejectedExecutionException e) {
                break;
            }

            vt = vt + dt;
            iter++;

            if(viewer != null){
                ArrayList<Body> newList = new ArrayList<>(bodies.stream().map(i -> i.clone()).collect(Collectors.toList()));
                this.viewer.display(newList, vt, iter, bounds);
            }

        }
        forkJoinPool.shutdown();
    }

    private void createBodies() {
        bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        int nBodies = 5000;
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
