package org.redi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by redi on 2015-10-17.
 */
public class Square {

    private int x,y;
    private int width, height;

    public Square(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(final ShapeRenderer shapeRenderer){

        shapeRenderer.rect(x , y, width, height);

    }
}
