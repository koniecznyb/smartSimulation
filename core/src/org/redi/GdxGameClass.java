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
import com.badlogic.gdx.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class GdxGameClass extends ApplicationAdapter {

    private final int MAP_WIDTH = 50, MAP_HEIGHT = 50;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
    private BitmapFont font;

    private Agent agent;

    private int timeStep = 0, renderTimer = 0, reward = 0;
    private Action prevAction, nextAction;
    private List<State> qValuesArray;

    private Environment environment;

    @Override
	public void create () {
		shapeRenderer = new ShapeRenderer();

        environment = Environment.getInstance();
        agent = new Agent(20, 20);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLUE);

		float h = Gdx.graphics.getHeight();
		float w = Gdx.graphics.getWidth();

		camera = new OrthographicCamera(60, 60 * (h/w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 3;

		Gdx.input.setInputProcessor(new MouseScroll(camera));

        environment.initializeMap();

        environment.populateEnvironment();

        initializeQValuesArray(qValuesArray);

    }

    private void initializeQValuesArray(List<State> qValuesArray) {

        qValuesArray = new ArrayList<>();
//        qValuesArray.add(new St);
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
//                nextAction = ;
            }

            agent.move(nextAction, Gdx.graphics.getDeltaTime());
            reward += agent.computeValueFunction(Environment.getInstance().getEnvironmentState(), nextAction);
            timeStep++;
            renderTimer = 1;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        agent.draw(shapeRenderer);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        environment.getObstacleList().stream().forEach(x -> x.draw(shapeRenderer));
        environment.getObstacleList().stream().forEach(x -> x.applyMovement(Gdx.graphics.getDeltaTime()));
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        environment.getPrizeList().stream().forEach(x -> x.draw(shapeRenderer));
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


    private void debugMap() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);

        Environment.MAP_FIELD[][] environment = Environment.getInstance().getEnvironmentState();

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(environment[i][j] == Environment.MAP_FIELD.OBSTACLE){
                    shapeRenderer.point(i,j, 0);
                }
                else{

                }
            }
        }
        shapeRenderer.end();
    }
s}
