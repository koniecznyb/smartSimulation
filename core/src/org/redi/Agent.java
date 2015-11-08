package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.org.apache.xpath.internal.SourceTree;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * Created by redi on 2015-10-18.
 */
@Getter
@Setter
public class Agent {

    private float x, y;
    private int width = 1;
    private int height = 1;
    private float speedPerSecond = 5;

    private float fitness = 0;

    public Agent(float x, float y) {
        this.y = y;
        this.x = x;
    }


    public int computeValueFunction(int [][] currentState, Action action){
        int reward = 0;

        if(action == Action.MOVE_DOWN){
            if(currentState[x][y+1] == 1){

            }
        }
        if(action == Action.MOVE_LEFT){

        }
        if(action == Action.MOVE_RIGHT){

        }
        if(action == Action.MOVE_UP){

        }

        System.out.println("Moving " + action + " in position [" + x + "][" + y + "], equals " +  reward);


        return reward;
    }

    public Action chooseAction (int [][] environment){

        return Action.MOVE_DOWN;
    }

    public void draw(final ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(x, y, width, height);
    }

    public void move(Direction direction, float deltaTime){
        if(direction == Direction.UP){
            y = y + (speedPerSecond * deltaTime);
        }
        if(direction == Direction.DOWN){
            y = y - (speedPerSecond * deltaTime);
        }
        if(direction == Direction.LEFT){
            x = x - (speedPerSecond * deltaTime);
        }
        if(direction == Direction.RIGHT){
            x = x + (speedPerSecond * deltaTime);
        }
    }

    public void increaseFitness(float amount){
        fitness += amount;
    }
    public void decreaseFitness(float amount){
        fitness -= amount;
    }

}
