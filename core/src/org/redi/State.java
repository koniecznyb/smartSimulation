package org.redi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by redi on 2015-11-15.
 */
@Getter
@Setter
@ToString
public class State {

    private int agentPositionX;
    private int agentPositionY;

    private int mapObjectPositionX;
    private int mapObjectPositionY;

    private Environment.MAP_FIELD mapObjectType;

    private State(int mapObjectPositionX, int mapObjectPositionY, Environment.MAP_FIELD mapObjectType) {
        this.mapObjectPositionX = mapObjectPositionX;
        this.mapObjectPositionY = mapObjectPositionY;
        this.mapObjectType = mapObjectType;
    }

    public static State with(int mapObjectPositionX, int mapObjectPositionY, Environment.MAP_FIELD mapObjectType){
        return new State(mapObjectPositionX, mapObjectPositionY, mapObjectType);
    }

    public static State getState(int [][] objectSurroundings){

        return null;

    }
}
