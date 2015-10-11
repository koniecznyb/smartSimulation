package org.redi;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by redi on 2015-10-04.
 */
public class MouseScroll implements InputProcessor {

    private final OrthographicCamera camera;

    public MouseScroll(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if(amount>0){
            camera.zoom = MathUtils.clamp(camera.zoom + 0.5f, 1.0f, 10.0f);
        }
        else if(amount<0){
            camera.zoom = MathUtils.clamp(camera.zoom - 0.5f, 1.0f, 10.0f);
        }
        return true;
    }
}
