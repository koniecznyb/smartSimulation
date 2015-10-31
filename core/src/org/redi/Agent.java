package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by redi on 2015-10-18.
 */
public class Agent {

    private int x, y, width = 1, height = 1;

    public Agent(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Action chooseAction (int [][] environment){

        return Action.MOVE_DOWN;
    }

    public void draw(final ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(x, y, width, height);
    }

    public void move(Direction direction){
        if(direction == Direction.UP){
            y++;
        }
        if(direction == Direction.DOWN){
            y--;
        }
        if(direction == Direction.LEFT){
            x--;
        }
        if(direction == Direction.RIGHT){
            x++;
        }
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
