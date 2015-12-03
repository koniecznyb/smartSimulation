package org.redi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by redi on 2015-11-15.
 */
@Getter
@Setter
@EqualsAndHashCode
public class State implements Comparable<State>, Serializable {

    private static int statesID = 0;
    public int stateID = statesID++;
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
            System.out.println(Arrays.deepToString(agentSurroundings));
            throw new RuntimeException(e);
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
