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

    private Map<Environment.MAP_FIELD, Double> rewardValues;

    private int x, y, width = 1, height = 1;
    private float speedPerSecond = 1;

    public Agent(int x, int y) {
        this.y = y;
        this.x = x;

        rewardValues = new HashMap<>();
        rewardValues.put(Environment.MAP_FIELD.EMPTY, 0d);
        rewardValues.put(Environment.MAP_FIELD.OBSTACLE, 10d);
        rewardValues.put(Environment.MAP_FIELD.PRIZE, 10d);
        rewardValues.put(Environment.MAP_FIELD.BORDER, -200d);

    }


    public double returnReward(State currentState, Action currentAction){

        switch (currentAction){
            case MOVE_DOWN:{
                return rewardValues.get(currentState.getBottom());
            }
            case MOVE_UP:{
                return rewardValues.get(currentState.getTop());
            }
            case MOVE_RIGHT:{
                return rewardValues.get(currentState.getRight());
            }
            case MOVE_LEFT:{
                return rewardValues.get(currentState.getLeft());
            }
            default:{
                return 0;
            }
        }
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


//    manual movement
//    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//        agent.move(Action.MOVE_LEFT, Gdx.graphics.getDeltaTime());
//    }
//    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//        agent.move(Action.MOVE_RIGHT, Gdx.graphics.getDeltaTime());
//    }
//    if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//        agent.move(Action.MOVE_DOWN, Gdx.graphics.getDeltaTime());
//    }
//    if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
//        agent.move(Action.MOVE_UP, Gdx.graphics.getDeltaTime());
//    }


}
