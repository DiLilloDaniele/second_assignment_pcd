package simulator.controller;

import simulator.SimulationView;

import java.util.concurrent.ExecutionException;

public interface Controller {

    void execute(long nSteps, SimulatorWithExecutors.ComputationType type) throws InterruptedException, ExecutionException;

    void setView(SimulationView view);

    void stopExecution();

    void executeFromGUI(long i, SimulatorWithExecutors.ComputationType forkJoin);
}
