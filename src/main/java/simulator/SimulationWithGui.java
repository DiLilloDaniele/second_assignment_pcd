package simulator;

import simulator.SimulationView;
import simulator.controller.Controller;
import simulator.controller.SimulatorWithExecutors;

public class SimulationWithGui {
    private static final long N_STEPS = 1000;

    public static void main(String[] args) {

        Controller sim = new SimulatorWithExecutors();
        SimulationView viewer = new SimulationView(620,620, sim);
        sim.setView(viewer);

    }
}
