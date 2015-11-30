package org.redi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

/**
 * Created by Bartłomiej Konieczny on 2015-11-30.
 */
public class QLearningAlgorithm implements Algorithm {

    private int timeStep = 0, reward = 0;
    private Map<State, Map<Action, Integer>> qValues;

    double gamma = 0.95;
    float epsilon = 0.2f;

    private int threshold = 1;
    private int rate = 1;

    public void slowDownSimulation(){
        threshold += rate;
        threshold = MathUtils.clamp(threshold, 1, 1000);
    }

    public void speedUpSimulation(){
        threshold -= rate;
        threshold = MathUtils.clamp(threshold, 1, 1000);
    }


    @Override
    public int run(Agent agent, int renderTimer) {

        State consideredNextState;
        State currentState;
        Action considerAction;

        if(renderTimer % threshold == 0){
            double d = Math.random();

            currentState = State.identifyState(agent.getX(), agent.getY(), Environment.getInstance().getEnvironmentState());
            Map<Action, Integer> currentStateQValues = qValues.get(currentState);
            List<Action> currentlyPossibleActions = new ArrayList<>(currentStateQValues.keySet());

    //           Select one among all possible actions for the current state
    //           epsilon-greedy policy takes random action once in a while
            if (d < epsilon){
                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));

            }
            else {
                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));
            }

    //            Using this possible action, consider going to the next state.
            consideredNextState = State.considerNextState(considerAction, agent.getX(), agent.getY());
            Map<Action, Integer> consideredStateQValues = qValues.get(consideredNextState);
            List<Action> consideredPossibleActions = new ArrayList<>(consideredStateQValues.keySet());

            double Qmax = 0;

    //            Get maximum Q value for this next state based on all possible actions.
            for(Action action : consideredPossibleActions){
                if(consideredStateQValues.get(action) > Qmax){
                    Qmax = consideredStateQValues.get(action);
                }
            }

    //            Compute: Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all actions)]
            int reward = agent.returnReward();
            double currentStateQValue = qValues.get(currentState).get(considerAction);

            double qValue = currentStateQValue + reward + gamma*(Qmax);

            qValues.get(currentState).put(considerAction, (int) qValue);


    //            if (d < epsilon){
    ////           epsilon-greedy policy  takes random action once in a while
    //                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
    //
    //            }
    //            else {
    //                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
    ////                possibleActions.stream().forEach(System.out::println);
    //
    //            }

            agent.move(considerAction, Gdx.graphics.getDeltaTime());
            timeStep++;
            return reward;

        }
        return 0;
    }

    @Override
    public Map<State, Map<Action, Integer>> initializeQValuesArray() {
        Map<Action, Integer> actionValues = new HashMap<>();
        List<Action> possibleActions = Arrays.asList(Action.MOVE_DOWN, Action.MOVE_LEFT, Action.MOVE_RIGHT, Action.MOVE_UP);

        possibleActions.stream().forEach(x -> actionValues.put(x, 0));
        qValues = new TreeMap<>();
        Environment.getPossibleStatesList().stream()
                .forEach(state -> qValues.put(state, new HashMap<>(actionValues)));

//        gdy robot znajduje się w pobliżu granicy to jego akcje są ograniczone
        Environment.getPossibleStatesList().stream()
                .filter(findBorderState)
                .forEach(state -> {
                    Environment.MAP_FIELD [][] agentSurroundings = state.getAgentSurroundings();
                    Map<Action, Integer> modifiedMap = new HashMap<>(actionValues);

//                    left
                    if(agentSurroundings[0][1] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_LEFT);
                    }
//                    right
                    if(agentSurroundings[2][1] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_RIGHT);
                    }
//                    top
                    if(agentSurroundings[1][2] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_UP);
                    }
//                    bottom
                    if(agentSurroundings[1][0] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_DOWN);
                    }
                    qValues.put(state, modifiedMap);

                });

        return qValues;
    }

    Predicate<State> findBorderState = state -> {
        for(Environment.MAP_FIELD[] row : state.getAgentSurroundings()){
            for(Environment.MAP_FIELD currentField : row){
                if(currentField != null && currentField.equals(Environment.MAP_FIELD.BORDER))
                    return true;
            }
        }
        return false;
    };

}
