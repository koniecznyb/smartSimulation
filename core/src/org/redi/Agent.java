/**
 Copyright (c) 2015, Bartłomiej Konieczny
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the Bartłomiej Konieczny.
 4. Neither the name of the Bartłomiej Konieczny nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Bartłomiej Konieczny ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Bartłomiej Konieczny BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Class defining an intelligent agent, that learns the environment.
 *     Agent gains knowledge about the environment based on defined rewards he receives when executing an {@link Action}.
 *     Rewards are pre-definied in {@link Agent#rewardValues}
 * </p>
 * Created by Bartłomiej Konieczny on 2015-10-18.
 */
@Getter
@Setter
public class Agent {

    private Map<Environment.MAP_FIELD, Float> rewardValues;

    private int x, y, width = 1, height = 1;
    private float speedPerSecond = 1;

    public Agent(int x, int y) {
        this.y = y;
        this.x = x;

        rewardValues = new HashMap<>();
        rewardValues.put(Environment.MAP_FIELD.EMPTY, -1f);
        rewardValues.put(Environment.MAP_FIELD.OBSTACLE, -10f);
        rewardValues.put(Environment.MAP_FIELD.PRIZE, 100f);
        rewardValues.put(Environment.MAP_FIELD.BORDER, -200f);

    }


    /**
     * Returns the reward when executing action in state, rewards are based on the type of field that agent encounters.
     * <p>
     *     Bonus points when moving towards the goal.
     * </p>
     * @param currentState state in which action is executed
     * @param currentAction action which is executed
     * @param agentX X coordiante of an agent
     * @param agentY Y coordinate of an agent
     * @return computed reward based on field and whether moving towards the goal or not
     */
    public float returnReward(State currentState, Action currentAction, int agentX, int agentY){

        switch (currentAction){
            case MOVE_DOWN:{
//                if moving towards goal
                if(Environment.getGoalY() < agentY){
                    return rewardValues.get(currentState.getTop()) + 5f;
                }
                return rewardValues.get(currentState.getBottom());
            }
            case MOVE_UP:{
                if(Environment.getGoalY() > agentY){
                    return rewardValues.get(currentState.getTop()) + 5f;
                }
                return rewardValues.get(currentState.getTop());

            }
            case MOVE_RIGHT:{
                if(Environment.getGoalX() > agentX){
                    return rewardValues.get(currentState.getTop()) + 5f;
                }
                return rewardValues.get(currentState.getRight());
            }
            case MOVE_LEFT:{
                if(Environment.getGoalX() < agentX){
                    return rewardValues.get(currentState.getTop()) + 5f;
                }
                return rewardValues.get(currentState.getLeft());
            }
            default:{
                return 0;
            }
        }
    }


    /**
     * Draws the agent on a map.
     * @param shapeRenderer renderer to draw to
     */
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
