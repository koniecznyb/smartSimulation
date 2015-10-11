package org.redi;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by redi on 2015-10-10.
 */
public class Map {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private Sprite mapSprite = Resources.getInstance().mapSprite;

    public void init(){
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
    }


    public void draw(SpriteBatch batch) {
        mapSprite.draw(batch);
    }
}
