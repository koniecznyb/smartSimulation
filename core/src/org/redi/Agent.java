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

    private int x, y, width = 1, height = 1;
    private float speedPerSecond = 1;

    private float fitness = 0;

    public Agent(int x, int y) {
        this.y = y;
        this.x = x;

        rewardValues = new HashMap<>();
        rewardValues.put(Environment.MAP_FIELD.EMPTY, -1);
        rewardValues.put(Environment.MAP_FIELD.OBSTACLE, -5);
        rewardValues.put(Environment.MAP_FIELD.PRIZE, 10);
        rewardValues.put(Environment.MAP_FIELD.BORDER, -200);

    }


    public int returnReward(){
        Environment.MAP_FIELD environmentType = Environment.getInstance().getEnvironmentState()[x][y];
        return rewardValues.get(environmentType);
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
