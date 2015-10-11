package org.redi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by redi on 2015-10-10.
 */
public class Resources {

    public Sprite mapSprite = new Sprite(new Texture("ground.jpg"));
    public Sprite tankSprite = new Sprite(new TextureRegion(new Texture("vehicles.png"), 302, 142, 192, 454));

    public static Resources instance;

    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    private Resources() {
    }
}
