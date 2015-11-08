package org.redi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GdxGameClass extends ApplicationAdapter {

    private final int NUMBER_OF_BOXES = 6;
    private final int MAP_WIDTH = 50, MAP_HEIGHT = 50;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

    private List<Square> squareList = new ArrayList<Square>();

    private SpriteBatch batch;
    private BitmapFont font;

    private Agent agent;

    private int timeStep = 0, renderTimer = 0;


    @Override
	public void create () {
		shapeRenderer = new ShapeRenderer();

        agent = new Agent(20f, 20f);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLUE);

		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();

		camera = new OrthographicCamera(60, 60 * (h/w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 3;
		camera.update();

		Gdx.input.setInputProcessor(new MouseScroll(camera));

        initializeMap();
        populateEnvironment(NUMBER_OF_BOXES);

    }

    private void debugMap() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);

        int [][] environment = Environment.getInstance();

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(environment[i][j] == 1){
                    shapeRenderer.point(i,j, 0);
                }
                else{

                }
            }
        }
        shapeRenderer.end();
    }

    private void initializeMap() {
        int [][] environment = Environment.getInstance();

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                environment[i][j] = 0;
            }
        }
    }

    @Override
    public void render () {

        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        renderTimer++;

        if(renderTimer % 60 == 0){

            agent.computeValueFunction(Environment.getInstance(), Action.MOVE_DOWN);
            timeStep++;
            renderTimer = 1;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        agent.draw(shapeRenderer);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        squareList.stream().forEach(x -> x.draw(shapeRenderer));
        squareList.stream().forEach(x -> x.applyMovement(Gdx.graphics.getDeltaTime()));
        shapeRenderer.end();

        drawBox(MAP_WIDTH, MAP_HEIGHT);
        debugMap();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            agent.move(Direction.LEFT, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            agent.move(Direction.RIGHT, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            agent.move(Direction.DOWN, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            agent.move(Direction.UP, Gdx.graphics.getDeltaTime());
        }


    }

    private void drawBox(int map_width, int map_height) {
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(0, 0, map_width, map_height);
        shapeRenderer.end();
    }


    @Override
	public void resize(int width, int height) {
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
	}


    private void populateEnvironment(int numberOfBoxes){
        for(int i=0; i<numberOfBoxes; i++){
            int randomWidth = ThreadLocalRandom.current().nextInt(1, 5);
            int randomHeight = ThreadLocalRandom.current().nextInt(1, 5);

            int randomX = ThreadLocalRandom.current().nextInt(1, MAP_WIDTH-randomWidth);
            int randomY = ThreadLocalRandom.current().nextInt(1, MAP_HEIGHT-randomHeight);

            int [][] environment = Environment.getInstance();

            for(int j=0; j<randomWidth; j++){
                for(int k=0; k<randomHeight; k++){
                    if( environment[j+randomX][k+randomY] == 1){
                        i--;
                        continue;
                    }
                }
            }

            squareList.add(new Square(randomX, randomY, randomWidth, randomHeight));

            for(int j=0; j<randomWidth; j++){
                for(int k=0; k<randomHeight; k++){
                    environment[j+randomX][k+randomY] = 1;
                }
            }
        }
    }



}
