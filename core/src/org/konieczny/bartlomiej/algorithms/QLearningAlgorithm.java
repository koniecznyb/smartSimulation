/**
 Copyright (c) 2015, Bartłomiej Konieczny
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the Bartłomiej Konieczny.
 4. Neither the name of the Bartłomiej Konieczny nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Bartłomiej Konieczny ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Bartłomiej Konieczny BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.konieczny.bartlomiej.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import lombok.Setter;
import org.konieczny.bartlomiej.model.Action;
import org.konieczny.bartlomiej.model.Agent;
import org.konieczny.bartlomiej.model.Environment;
import org.konieczny.bartlomiej.model.State;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

/**
 * <p>
 *     Q-Learning algorithm, implementation of reinforcement algorithm class.
 *     Q-learning is a model-free reinforcement learning technique, it works by learning an action-value function that
 *     ultimately gives the expected utility of taking a given action in a given state and
 *     following the optimal policy thereafter. A policy is a rule that the agent follows in selecting actions,
 *     given the state it is in.
 * </p>
 * <p>
 *     Off-policy algorithm.
 * </p>
 * Created by Bartłomiej Konieczny on 2015-11-30.
 */
public class QLearningAlgorithm implements Algorithm {

    private int timeStep = 0;
    private int reward = 0;
    private Map<State, Map<Action, Float>> qValues;

    float discountFactor = 1f;
    float learningRate = 1f;
    @Setter float epsilon = 0.2f;

    private int threshold = 1;
    private int rate = 1;

    @Override
    public void slowDown(){
        threshold += rate;
        threshold = MathUtils.clamp(threshold, 1, 1000);
    }

    @Override
    public int getTimeStep() {
        return timeStep;
    }

    @Override
    public void reset() {
        timeStep = 0;
    }

    @Override
    public void speedUp(){
        threshold -= rate;
        threshold = MathUtils.clamp(threshold, 1, 1000);
    }

    @Override
    public double run(Agent agent, int renderTimer) {

        State currentState, nextState;
        Action currentAction;

        if(renderTimer % threshold == 0){

//            start out in an initial state as the current state
            currentState = State.identifyState(agent.getX(), agent.getY(), Environment.getInstance().getEnvironmentState());
            List<Action> currentlyPossibleActions = new ArrayList<>(qValues.get(currentState).keySet());

//            select one among all possible actions for the current state
//            epsilon-greedy policy takes random action once in a while, otherwise chooses the best one
            double d = Math.random();
            if (d > epsilon){

//                get best action for current state
                double Qmax = qValues.get(currentState).get(currentlyPossibleActions.get(0));
                Action bestAction = null;

                for(Map.Entry<Action, Float> actionEntry : qValues.get(currentState).entrySet()){
                    if(actionEntry.getValue() >= Qmax){
                        Qmax = actionEntry.getValue();
                        bestAction = actionEntry.getKey();
                    }
                }
                if(bestAction == null){
                    for(Map.Entry<Action, Float> actionEntry : qValues.get(currentState).entrySet())
                        System.out.println(actionEntry);
                    throw new NullPointerException("Empty action!");
                }
                currentAction = bestAction;
            }
            else {
//                random action
                currentAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));
            }

//            consider transitioning to state, after executing current action
            nextState = State.considerNextState(currentAction, agent.getX(), agent.getY());

//            find qMax for nextState
            float Qmax = 0;

            for(Map.Entry<Action, Float> actionEntry : qValues.get(nextState).entrySet()){
                if(actionEntry.getValue() > Qmax){
                    Qmax = actionEntry.getValue();
                }
            }

//            get the reward for the (current state, current action, next state) tuple
            float reward = agent.returnReward(currentState, currentAction, agent.getX(), agent.getY());


//            update Q value
            float currentStateQValue = qValues.get(currentState).get(currentAction);
            float qValue = currentStateQValue + learningRate *(reward + discountFactor *Qmax - currentStateQValue);
            qValues.get(currentState).put(currentAction, qValue);


//            set current state to next state, NOT NEEDED
            currentState = nextState;

            agent.move(currentAction, Gdx.graphics.getDeltaTime());
            timeStep++;

            return reward;
        }

        return 0;
    }
//    @Override
//    public int run(Agent agent, int renderTimer) {
//
//        State consideredNextState;
//        State currentState;
//        Action considerAction;
//
//        if(renderTimer % threshold == 0){
//            double d = Math.random();
//
//            currentState = State.identifyState(agent.getX(), agent.getY(), Environment.getInstance().getEnvironmentState());
//            Map<Action, Integer> currentStateQValues = qValues.get(currentState);
//            List<Action> currentlyPossibleActions = new ArrayList<>(currentStateQValues.keySet());
//
//    //           Select one among all possible actions for the current state
//    //           epsilon-greedy policy takes random action once in a while
//            if (d < epsilon){
//                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));
//
//            }
//            else {
//                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));
//            }
//
//    //            Using this possible action, consider going to the next state.
//            consideredNextState = State.considerNextState(considerAction, agent.getX(), agent.getY());
//            Map<Action, Integer> consideredStateQValues = qValues.get(consideredNextState);
//            List<Action> consideredPossibleActions = new ArrayList<>(consideredStateQValues.keySet());
//
//            double Qmax = 0;
//
//    //            Get maximum Q value for this next state based on all possible actions.
//            for(Action action : consideredPossibleActions){
//                if(consideredStateQValues.get(action) > Qmax){
//                    Qmax = consideredStateQValues.get(action);
//                }
//            }
//
//    //            Compute: Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all actions)]
//            int reward = agent.returnReward();
//            double currentStateQValue = qValues.get(currentState).get(considerAction);
//
//            double qValue = currentStateQValue + reward + learningRate*(Qmax);
//
//            qValues.get(currentState).put(considerAction, (int) qValue);
//
//
//    //            if (d < epsilon){
//    ////           epsilon-greedy policy  takes random action once in a while
//    //                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
//    //
//    //            }
//    //            else {
//    //                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
//    ////                possibleActions.stream().forEach(System.out::println);
//    //
//    //            }
//
//            agent.move(considerAction, Gdx.graphics.getDeltaTime());
//            timeStep++;
//            return reward;
//
//        }
//        return 0;
//    }

    @Override
    public Map<State, Map<Action, Float>> initializeQValuesArray() {
        Map<Action, Float> actionValues = new HashMap<>();
        List<Action> possibleActions = Arrays.asList(Action.MOVE_DOWN, Action.MOVE_LEFT, Action.MOVE_RIGHT, Action.MOVE_UP);

        possibleActions.stream().forEach(x -> actionValues.put(x, 0f));
        qValues = new TreeMap<>();
        Environment.getPossibleStatesList().stream()
                .forEach(state -> qValues.put(state, new HashMap<>(actionValues)));

//        gdy robot znajduje się w pobliżu granicy to jego akcje są ograniczone
        Environment.getPossibleStatesList().stream()
                .filter(findBorderState)
                .forEach(state -> {
                    Environment.MAP_FIELD [][] agentSurroundings = state.getAgentSurroundings();
                    Map<Action, Float> modifiedMap = new HashMap<>(actionValues);

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
