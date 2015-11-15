package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by redi on 2015-10-18.
 */
@Getter
@Setter
public class Agent {

    private Map<Environment.MAP_FIELD, Integer> rewardValues;

    private int x;
    private int y;
    private int width = 1;
    private int height = 1;
    private float speedPerSecond = 1;

    private float fitness = 0;

    public Agent(int x, int y) {
        this.y = y;
        this.x = x;

        rewardValues = new HashMap<>();
        rewardValues.put(Environment.MAP_FIELD.EMPTY, -1);
        rewardValues.put(Environment.MAP_FIELD.OBSTACLE, -5);
        rewardValues.put(Environment.MAP_FIELD.PRIZE, 10);
    }


    public int computeValueFunction(Environment.MAP_FIELD[][] currentState, Action action){
        int reward = 0;

        if(action == Action.MOVE_LEFT){
            reward = returnReward(currentState[x-1][y]);
        }
        if(action == Action.MOVE_RIGHT){
            reward = returnReward(currentState[x+1][y]);
        }
        if(action == Action.MOVE_DOWN){
            reward = returnReward(currentState[x][y+1]);
        }
        if(action == Action.MOVE_UP){
            reward = returnReward(currentState[x][y+1]);
        }
        System.out.println("Moving " + action + " in position [" + x + "][" + y + "], equals " +  reward);
        return reward;
    }

    private int returnReward(Environment.MAP_FIELD environmentType){
        return rewardValues.get(environmentType);
    }

    public Action chooseAction (int [][] environment){

        return Action.MOVE_DOWN;
    }

    public void draw(final ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(x, y, width, height);
    }

    public void move(Action action, float deltaTime){
        if(action == Action.MOVE_UP){
//            y = y + (int) (speedPerSecond * deltaTime);
            y += speedPerSecond;
        }
        if(action == Action.MOVE_DOWN){
//            y = y - (int) (speedPerSecond * deltaTime);
            y -= speedPerSecond;
        }
        if(action == Action.MOVE_LEFT){
//            x = x - (int) (speedPerSecond * deltaTime);
            x -= speedPerSecond;
        }
        if(action == Action.MOVE_RIGHT){
//            x = x + (int) (speedPerSecond * deltaTime);
            x += speedPerSecond;
        }
    }

    public void increaseFitness(float amount){
        fitness += amount;
    }
    public void decreaseFitness(float amount){
        fitness -= amount;
    }

}
