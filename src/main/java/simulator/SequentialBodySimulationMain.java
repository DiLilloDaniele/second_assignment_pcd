package simulator;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class SequentialBodySimulationMain {

    public static void main(String[] args) {

    	//SimulationView viewer = new SimulationView(620,620);

    	Simulator sim = new Simulator();
        long start = System.currentTimeMillis();
        sim.execute(5000);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("TEMPO TRASCORSO: " + timeElapsed);
    }
}
