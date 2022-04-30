import simulator.SimulationView;
import simulator.Simulator;
import simulator.controller.SimulatorWithExecutors;

import java.util.concurrent.ExecutionException;

public class ExecutorBodySimulationMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //SimulationView viewer = new SimulationView(620,620);

        SimulatorWithExecutors sim = new SimulatorWithExecutors();
        long start = System.currentTimeMillis();
        sim.executeWithFutures(10000);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("TEMPO TRASCORSO: " + timeElapsed);
    }
}
