package org.redi;

import java.util.Map;

/**
 * Created by redi on 2015-11-11.
 */
public interface Algorithm {

    double run(final Agent agent, int renderTimer);
    Map<State, Map<Action, Integer>> initializeQValuesArray();

    void speedUp();
    void slowDown();

}
