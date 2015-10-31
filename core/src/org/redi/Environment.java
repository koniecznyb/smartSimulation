package org.redi;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by redi on 2015-10-17.
 */
public class Environment {

    private int [][] environmentState;

    public Environment(int[][] environmentState) {
        this.environmentState = environmentState;
    }

    public int respond(Action a, int agentPositionX, int agentPositionY){

        return 2;
    }

}
