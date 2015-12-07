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
package org.konieczny.bartlomiej.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.konieczny.bartlomiej.simulation.Environment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 *     Class representing state in a simulation.
 *     A state is an abstract representation of the environment around an {@link Agent}.
 * </p>
 * Created by Bartłomiej Konieczny on 2015-11-15.
 */
@Getter
@Setter
@EqualsAndHashCode
public class State implements Comparable<State>, Serializable {

    private static int statesID = 0;
    public int stateID = statesID++;
    private Environment.MAP_FIELD[][] agentSurroundings;

    private State(Environment.MAP_FIELD[][] agentSurroundings) {
        this.agentSurroundings = agentSurroundings;
    }


    public static State with(Environment.MAP_FIELD [][] agentSurroundings){
        return new State(agentSurroundings);
    }

    /**
     * Returns the state from a list of possible states {@link Environment#getPossibleStatesList()}, based on the surroundings of an {@link Agent}.
     * @param agentPositionX X coordinate of an agent
     * @param agentPositionY Y coordinate of an agent
     * @param currentMapState current state of the environment
     * @return identified state
     */
    public static State identifyState(int agentPositionX, int agentPositionY, Environment.MAP_FIELD [][] currentMapState){
        Environment.MAP_FIELD [][] agentSurroundings =  getAgentSurroundings(agentPositionX, agentPositionY, currentMapState);
        List<State> possibleStates = Environment.getPossibleStatesList();

        State identifiedState = null;
        try{
            identifiedState = possibleStates.stream()
                    .filter(state -> Arrays.deepEquals(state.agentSurroundings, agentSurroundings))
                    .findFirst().get();
        }catch (NoSuchElementException e){
            System.out.println(Arrays.deepToString(agentSurroundings));
            throw new RuntimeException(e);
        }
       return identifiedState;
    }

    /**
     * Allows to predict in what {@link State} will an agent be, after executing specified {@link Action}.
     * @param action action to be executed
     * @param agentX X coordinate of an agent
     * @param agentY Y coordinate of an agent
     * @return considered new state
     */
    public static State considerNextState(Action action, int agentX, int agentY){
        int newX = agentX;
        int newY = agentY;

        switch (action){
            case MOVE_DOWN:{
                newY = agentY - 1;
                break;
            }
            case MOVE_LEFT:{
                newX = agentX - 1;
                break;
            }
            case MOVE_RIGHT:{
                newX = agentX + 1;
                break;
            }
            case MOVE_UP:{
                newY = agentY + 1;
                break;
            }
        }
        return State.identifyState(newX, newY, Environment.getInstance().getEnvironmentState());
    }


    /**
     * Returns the surroundings of an {@link Agent} based on its position and current map state - {@link Environment#environmentState}.
     * @param agentPositionX X coordinate of an agent
     * @param agentPositionY Y coordinate of an agent
     * @param currentMapState current map state
     * @return surroundings of an agent in form: int [2][2]
     */
    private static Environment.MAP_FIELD[][] getAgentSurroundings(int agentPositionX, int agentPositionY, Environment.MAP_FIELD[][] currentMapState) {

        Environment.MAP_FIELD [][] agentSurroundings = new Environment.MAP_FIELD[3][3];
        for(Environment.MAP_FIELD [] mapFields : agentSurroundings){
            Arrays.fill(mapFields, null);
        }

        //        lewa
        agentSurroundings[0][1] = currentMapState[agentPositionX - 1][agentPositionY];
        //        prawa
        agentSurroundings[2][1] = currentMapState[agentPositionX + 1][agentPositionY];
        //        dół
        agentSurroundings[1][0] = currentMapState[agentPositionX][agentPositionY - 1];
        //        góra
        agentSurroundings[1][2] = currentMapState[agentPositionX][agentPositionY + 1];

        return agentSurroundings;
    }

    private static Environment.MAP_FIELD[][] deepCopy(Environment.MAP_FIELD[][] original) {
        if (original == null) {
            return null;
        }

        final Environment.MAP_FIELD[][] result = new Environment.MAP_FIELD[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public Environment.MAP_FIELD getLeft(){
        return agentSurroundings[0][1];
    }

    public Environment.MAP_FIELD getRight(){
        return agentSurroundings[2][1];
    }

    public Environment.MAP_FIELD getTop(){
        return agentSurroundings[1][2];
    }

    public Environment.MAP_FIELD getBottom(){
        return agentSurroundings[1][0];
    }

    @Override
    public String toString() {
        StringBuilder stateString = new StringBuilder();

        for(int y = agentSurroundings[0].length-1; y>= 0; y--){
            for(int x = 0; x<agentSurroundings.length; x++){
                if(x==1 && y == 1){
                    stateString.append(getStateID());
                }
                else if(agentSurroundings[x][y] == null){
                    stateString.append("X");
                }
                else if(agentSurroundings[x][y] == Environment.MAP_FIELD.OBSTACLE){
                    stateString.append("O");
                }
                else if(agentSurroundings[x][y] == Environment.MAP_FIELD.BORDER) {
                    stateString.append("B");
                }
                else if(agentSurroundings[x][y] == Environment.MAP_FIELD.EMPTY){
                    stateString.append("E");
                }
                else {
                    stateString.append(agentSurroundings[x][y]);
                }
                stateString.append(" ");
            }
            stateString.append("\n");
        }

        return stateString.toString();
    }

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.getStateID(), o.getStateID());
    }
}
