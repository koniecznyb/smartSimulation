package org.redi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private final int NUMBER_OF_BOXES = 15;
    private final int MAP_WIDTH = 50, MAP_HEIGHT = 50;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

    private List<Square> squareList = new ArrayList<Square>();

    private SpriteBatch batch;
    private BitmapFont font;

    private Agent agent;

    private int timeStep = 0, renderTimer = 0, reward = 0;
    private Action prevAction, nextAction;

    @Override
	public void create () {
		shapeRenderer = new ShapeRenderer();

        agent = new Agent(20, 20);

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

        Environment.MAP_FIELD[][] environment = Environment.getInstance();

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
        Environment.MAP_FIELD[][] environment = Environment.getInstance();

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

        drawFitness(reward);

        renderTimer++;

        float epsilon = 0.2f;

        if(renderTimer % 30 == 0){

            double d = Math.random();
            if (d < epsilon){
//           epsilon-greedy policy  takes random action once in a while
                nextAction = Action.randomAction();
            }
            else {
                nextAction = ;
            }

            agent.move(nextAction, Gdx.graphics.getDeltaTime());
            reward += agent.computeValueFunction(Environment.getInstance(), nextAction);
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
//        debugMap();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            agent.move(Action.MOVE_LEFT, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            agent.move(Action.MOVE_RIGHT, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            agent.move(Action.MOVE_DOWN, Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            agent.move(Action.MOVE_UP, Gdx.graphics.getDeltaTime());
        }


    }

    private void drawFitness(int reward){
        batch.begin();
        font.draw(batch, "Fitness: " + reward, 100f, 120f);
        batch.end();

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

            Environment.MAP_FIELD[][] environment = Environment.getInstance();

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
                    environment[j+randomX][k+randomY] = Environment.MAP_FIELD.EMPTY;
                }
            }
        }
    }



}
