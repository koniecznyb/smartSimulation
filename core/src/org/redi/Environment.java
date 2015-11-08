package org.redi;

/**
 * Created by redi on 2015-10-17.
 */
public class Environment {

    private static int MAP_WIDTH = 50, MAP_HEIGHT = 50;
    private static int [][] environmentState;

    public Environment(int[][] environmentState) {
        this.environmentState = environmentState;
    }

    public static int [][] getInstance(){
        if(environmentState == null) {
            environmentState = new int[MAP_WIDTH][MAP_HEIGHT];
        }
        return environmentState;
    }

    public int respond(Action a, int agentPositionX, int agentPositionY){

        return 2;
    }

}
