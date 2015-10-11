package org.redi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class GdxGameClass extends ApplicationAdapter {

    Resources resources;
    SpriteBatch batch;

    Tank tank;
	OrthographicCamera camera;
	DynamicBody dynamicBody;

    World world;
    Box2DDebugRenderer debugRenderer;

    private Map map;

    Sprite mapSprite;


    @Override
	public void create () {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();

		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();

		camera = new OrthographicCamera(30, 30 * (h/w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 3;
		camera.update();

		Gdx.input.setInputProcessor(new MouseScroll(camera));


        tank = new Tank(camera, world, new Vector2(0, 0));
        tank.init();

        map = new Map();
        map.init();
    }

    @Override
    public void render () {

        camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.begin();
        map.draw(batch);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);


        for(Body body : bodies) {
            Sprite sprite = (Sprite) body.getUserData();

            sprite.setX(body.getPosition().x);
            sprite.setY(body.getPosition().y);
            sprite.draw(batch);
        }
        tank.handleInput();


        batch.end();
        debugRenderer.render(world, camera.combined);
        world.step(1 / 60f, 6, 2);
	}


	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
	}


}
