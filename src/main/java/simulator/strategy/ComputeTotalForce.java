package simulator.strategy;

import simulator.model.Body;
import simulator.model.V2d;

public interface ComputeTotalForce {

    V2d computeTotalForceOnBody(Body b);

}
