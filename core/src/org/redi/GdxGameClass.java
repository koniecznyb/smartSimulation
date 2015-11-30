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

import java.util.Map;

public class GdxGameClass extends ApplicationAdapter {

    private final int MAP_WIDTH = 50, MAP_HEIGHT = 50;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
    private BitmapFont font;

    private Agent agent;

    private Environment environment;
    private Algorithm QLearningAlgorithm;

    private int renderTimer = 0, reward = 0;

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

        camera = new OrthographicCamera(100, 100 * (h / w));
		camera.position.set(25, 50, 0);

        ExcelPrinter.createNewRunData(1);

        environment.initializeMap();
        environment.populateEnvironment();
        environment.initializePossibleStatesList();

        QLearningAlgorithm = new QLearningAlgorithm();
        QLearningAlgorithm.initializeQValuesArray();
    }

    @Override
    public void render () {

        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        renderTimer++;

        Map<State, Map<Action, Integer>> qValues = QLearningAlgorithm.initializeQValuesArray();
        reward += QLearningAlgorithm.run(agent, renderTimer);


        if(renderTimer%1000 == 0){
            ExcelPrinter.saveData(qValues);
        }

        drawFitness(reward);

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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        agent.draw(shapeRenderer);
        shapeRenderer.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN)){
            QLearningAlgorithm.slowDownSimulation();
            System.out.println("Spowalniam");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)){
            QLearningAlgorithm.speedUpSimulation();
            System.out.println("Przyspieszam");
        }

    }

    private void drawFitness(int reward){
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Fitness: " + reward, 150, 80);
        batch.end();

    }

    private void drawBox(int map_width, int map_height) {
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(0, 0, map_width, map_height);
        shapeRenderer.end();
    }

}
