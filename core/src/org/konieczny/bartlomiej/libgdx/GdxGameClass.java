/**
 Copyright (c) 2015, Bartłomiej Konieczny
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgement:
 This product includes software developed by the Bartłomiej Konieczny.
 4. Neither the name of the Bartłomiej Konieczny nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY Bartłomiej Konieczny ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL Bartłomiej Konieczny BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.konieczny.bartlomiej.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.konieczny.bartlomiej.simulation.Environment;
import org.konieczny.bartlomiej.helpers.ExcelPrinter;
import org.konieczny.bartlomiej.algorithms.Algorithm;
import org.konieczny.bartlomiej.algorithms.QLearningAlgorithm;
import org.konieczny.bartlomiej.model.Action;
import org.konieczny.bartlomiej.model.Agent;
import org.konieczny.bartlomiej.model.State;

import java.util.Map;

/**
 * Main class which start a simulation using LibGdx framework.
 */
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

    private Map<State, Map<Action, Float>> qValues;

    @Override
	public void create () {

		shapeRenderer = new ShapeRenderer();
        environment = Environment.getInstance();
        agent = new Agent(48, 2);

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
        qValues = QLearningAlgorithm.initializeQValuesArray();
    }

    @Override
    public void render () {

        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        renderTimer++;

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
            QLearningAlgorithm.slowDown();
            System.out.println("Spowalniam");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP)){
            QLearningAlgorithm.speedUp();
            System.out.println("Przyspieszam");
        }

    }


    /**
     * Draws current cumulative reward that agent has received.
     * @param reward
     */
    private void drawFitness(double reward){
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Fitness: " + reward, 150, 80);
        batch.end();

    }

    /**
     * Helper method for drawing a border of an map
     * @param map_width width of a box(map)
     * @param map_height height of a box(map)
     */
    private void drawBox(int map_width, int map_height) {
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(0, 0, map_width, map_height);
        shapeRenderer.end();
    }

}
