package org.redi;

/**
 * Created by redi on 2015-10-17.
 */
public class Environment {

    private static int MAP_WIDTH = 50, MAP_HEIGHT = 50;
    private static MAP_FIELD[][] environmentState;

    public Environment(MAP_FIELD[][] environmentState) {
        this.environmentState = environmentState;
    }

    public static MAP_FIELD[][] getInstance(){
        if(environmentState == null) {
            environmentState = new MAP_FIELD[MAP_WIDTH][MAP_HEIGHT];
        }
        return environmentState;
    }

    public int respond(Action a, int agentPositionX, int agentPositionY){

        return 2;
    }


    public enum MAP_FIELD {

        EMPTY(0), OBSTACLE(1), PRIZE(2);

        private int numValue;

        MAP_FIELD(int value) {
            this.numValue = value;
        }

        public int getNumValue() {
            return numValue;
        }
    }


}
