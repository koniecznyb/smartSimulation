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
import com.badlogic.gdx.utils.TimeUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GdxGameClass extends ApplicationAdapter {

    private final int MAP_WIDTH = 50, MAP_HEIGHT = 50;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
    private BitmapFont font;

    private Agent agent;

    private int timeStep = 0, renderTimer = 0, reward = 0;
    private Map<State, Map<Action, Integer>> qValues;

    private Environment environment;

    BufferedWriter qvaluesFile;

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
        environment.initializePossibleStatesList();

        initializeQValuesArray();

        try {
            qvaluesFile = new BufferedWriter(new FileWriter("qvalues.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeQValuesArray() {
        Map <Action, Integer> actionValues = new HashMap<>();
        List <Action> possibleActions = Arrays.asList(Action.MOVE_DOWN, Action.MOVE_LEFT, Action.MOVE_RIGHT, Action.MOVE_UP);

        possibleActions.stream().forEach(x -> actionValues.put(x, 0));
        qValues = new HashMap<>();
        Environment.getPossibleStatesList().stream()
                .forEach(state -> qValues.put(state, new HashMap<>(actionValues)));

//        gdy robot znajduje się w pobliżu granicy to jego akcje są ograniczone
        Environment.getPossibleStatesList().stream()
                .filter(findBorderState)
                .forEach(state -> {
                    Environment.MAP_FIELD [][] agentSurroundings = state.getAgentSurroundings();
                    Map<Action, Integer> modifiedMap = new HashMap<>(actionValues);

//                    left
                    if(agentSurroundings[0][1] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_LEFT);
                    }
//                    right
                    if(agentSurroundings[2][1] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_RIGHT);
                    }
//                    top
                    if(agentSurroundings[1][2] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_UP);
                    }
//                    bottom
                    if(agentSurroundings[1][0] == Environment.MAP_FIELD.BORDER){
                        modifiedMap.remove(Action.MOVE_DOWN);
                    }
                    qValues.put(state, modifiedMap);

                });
    }

    Predicate<State> findBorderState = state -> {
        for(Environment.MAP_FIELD[] row : state.getAgentSurroundings()){
            for(Environment.MAP_FIELD currentField : row){
                if(currentField != null && currentField.equals(Environment.MAP_FIELD.BORDER))
                    return true;
            }
        }
        return false;
    };


    @Override
    public void render () {

        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        drawFitness(reward);

        renderTimer++;

        double gamma = 0.95;
        double g = 0.95;

        float epsilon = 0.2f;

        State consideredNextState;
        State currentState;
        Action considerAction;

        if(renderTimer % 30 == 0){
            double d = Math.random();

            currentState = State.identifyState(agent.getX(), agent.getY(), Environment.getInstance().getEnvironmentState());
            Map<Action, Integer> currentStateQValues = qValues.get(currentState);
            List<Action> currentlyPossibleActions = new ArrayList<>(currentStateQValues.keySet());

//           Select one among all possible actions for the current state
//           epsilon-greedy policy takes random action once in a while
            if (d < epsilon){
                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));

            }
            else {
                considerAction = currentlyPossibleActions.get(ThreadLocalRandom.current().nextInt(0, currentlyPossibleActions.size()));
            }

//            Using this possible action, consider going to the next state.
            consideredNextState = State.considerNextState(considerAction, agent.getX(), agent.getY());
            Map<Action, Integer> consideredStateQValues = qValues.get(consideredNextState);
            List<Action> consideredPossibleActions = new ArrayList<>(consideredStateQValues.keySet());

            double Qmax = 0;

//            Get maximum Q value for this next state based on all possible actions.
            for(Action action : consideredPossibleActions){
                if(consideredStateQValues.get(action) > Qmax){
                    Qmax = consideredStateQValues.get(action);
                }
            }

//            Compute: Q(state, action) = R(state, action) + Gamma * Max[Q(next state, all actions)]
            int reward = agent.returnReward();
            double currentStateQValue = qValues.get(currentState).get(considerAction);

            double qValue = currentStateQValue + gamma*(Qmax);

            qValues.get(currentState).put(considerAction, (int) qValue);


//            if (d < epsilon){
////           epsilon-greedy policy  takes random action once in a while
//                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
//
//            }
//            else {
//                nextAction = possibleActions.get(ThreadLocalRandom.current().nextInt(0, possibleActions.size()));
////                possibleActions.stream().forEach(System.out::println);
//
//            }

            agent.move(considerAction, Gdx.graphics.getDeltaTime());
            timeStep++;
            renderTimer = 1;

            try {
                printToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        agent.draw(shapeRenderer);
        shapeRenderer.end();

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

    private void printToFile() throws IOException {

        Set<State> states = qValues.keySet();
//        states.forEach(state -> qvaluesFile.print("state"));

        for(Map<Action, Integer> actionIntegerMap : qValues.values()){
            for(Map.Entry<Action, Integer> entry : actionIntegerMap.entrySet()){
                if(entry.getValue() > 0) {
                    qvaluesFile.write(entry.getKey() + ": " + entry.getValue());
                }
                else{
                    qvaluesFile.write("none");
                }
            }
        }

//        qvaluesFile.println(qValues + "%\r");
//        System.out.print("\33[1A\33[2K");

    }

    private void drawFitness(int reward){
        batch.begin();
        font.setColor(Color.WHITE);
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

    @Override
    public void dispose() {
        super.dispose();
        try {
            qvaluesFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
