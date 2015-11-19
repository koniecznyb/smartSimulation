package org.redi;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.ToString;

import static com.badlogic.gdx.math.MathUtils.sin;

/**
 * Created by redi on 2015-10-17.
 */
@ToString
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

    public void applyMovement(float deltaTime) {
// TODO: 2015-11-08 Movement of the environment

    }
}
