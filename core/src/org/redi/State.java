package org.redi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by redi on 2015-11-15.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class State {

    private Environment.MAP_FIELD [][] agentSurroundings;

    private State(Environment.MAP_FIELD[][] agentSurroundings) {
        this.agentSurroundings = agentSurroundings;
    }


    public static State with(Environment.MAP_FIELD [][] agentSurroundings){
        return new State(agentSurroundings);
    }

    public static State identifyState(int agentPositionX, int agentPositionY, Environment.MAP_FIELD [][] currentMapState){
        Environment.MAP_FIELD [][] agentSurroundings =  getAgentSurroundings(agentPositionX, agentPositionY, currentMapState);
        List<State> possibleStates = Environment.getPossibleStatesList();

        State identifiedState = null;
        try{
            identifiedState = possibleStates.stream()
                    .filter(state -> Arrays.deepEquals(state.agentSurroundings, agentSurroundings))
                    .findFirst().get();
        }catch (NoSuchElementException e){
            System.out.println("not found state");
        }
       return identifiedState;
    }

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


    private static Environment.MAP_FIELD[][] getAgentSurroundings(int agentPositionX, int agentPositionY, Environment.MAP_FIELD[][] currentMapState) {

        Environment.MAP_FIELD [][] agentSurroundings = new Environment.MAP_FIELD[3][3];
        for(Environment.MAP_FIELD [] mapFields : agentSurroundings){
            Arrays.fill(mapFields, null);
        }

//        lewa
        agentSurroundings[0][1] = currentMapState[agentPositionX-1][agentPositionY];
//        prawa
        agentSurroundings[2][1] = currentMapState[agentPositionX+1][agentPositionY];
//        dół
        agentSurroundings[1][0] = currentMapState[agentPositionX][agentPositionY-1];
//        góra
        agentSurroundings[1][2] = currentMapState[agentPositionX][agentPositionY+1];

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

}
