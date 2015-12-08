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
package org.konieczny.bartlomiej.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *     Singleton class representing the environment in which agent is operating.
 *     Defines size of the map, current state of the environment, list of obstacles, list of prizes and
 *     all possible states, that can occur to an agent.
 * </p>
 * <p>
 *     Contains the X and Y coordinates of the goal object.
 * </p>
 * <p>
 *     Created by Bartłomiej Konieczny on 2015-10-17.
 * </p>
 *     @see Agent
 *     @see State
 *     @see Action
 */
public class Environment {

    private static Environment instance = null;
    private static final int NUMBER_OF_BOXES = 15, NUMBER_OF_PRIZES = 0, MAP_WIDTH = 50, MAP_HEIGHT = 50;
    @Getter private static final int goalX = 2, goalY = 48;
    @Getter private MAP_FIELD[][] environmentState;
    @Getter private List<Square> obstacleList = new ArrayList<>(), prizeList = new ArrayList<>();
    @Getter private static List<State> possibleStatesList = new ArrayList<>();


    private Environment(MAP_FIELD[][] environmentState) {
        this.environmentState = environmentState;
    }

    /**
     * Gets instance of singleton Environment variable;
     * @return singleton Environment variable
     */
    public static Environment getInstance(){
        if(instance == null) {
            instance = new Environment(new MAP_FIELD[MAP_WIDTH][MAP_HEIGHT]);
        }
        return instance;
    }

    public enum MAP_FIELD {

        EMPTY(0), OBSTACLE(1), PRIZE(2), BORDER(3);

        private int numValue;

        MAP_FIELD(int value) {
            this.numValue = value;
        }

        public int getNumValue() {
            return numValue;
        }
    }


    /**
     * Initializes the map with empty fields.
     */
    public void initializeMap() {

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                environmentState[i][j] = Environment.MAP_FIELD.EMPTY;
            }
        }
    }

    /**
     * Populates the environment with obstacles and prizes.
     */
    public void populateEnvironment(){
        spawnBox(20, 10, 25, 5);
        spawnBox(10, 20, 25, 5);
        spawnBox(0, 30, 25, 5);
        spawnBox(20, 40, 25, 2);
        spawnPrize(goalX, goalY);
        generateBorders();
//        generateRandomBoxes(NUMBER_OF_BOXES);
//        generateRandomPrizes(NUMBER_OF_PRIZES);

    }

    /**
     * Spawns one prize on given coordinates.
     * @param x x coordinate of a prize
     * @param y y coordinate of a prize
     */
    private void spawnPrize(int x, int y) {
        prizeList.add(new Square(x, y, 1, 1));
        environmentState[x][y] = MAP_FIELD.PRIZE;
    }

    /**
     * Spawns box on given coordinates with specific width and height.
     * @param x x coordinate of an obstacle
     * @param y y coordinate of an obstacle
     * @param width width of an obstacle
     * @param height height of an obstacle
     */
    private void spawnBox(int x, int y, int width, int height){
        obstacleList.add(new Square(x, y, width, height));

        for(int i=x; i<width+x; i++){
            for(int j=y; j<height+y; j++){
                environmentState[i][j] = MAP_FIELD.OBSTACLE;
            }
        }
    }


    /**
     * Generates the rectangle that simulates map borders.
     */
    private void generateBorders() {
        for(int i=0; i<MAP_WIDTH; i++){
            environmentState[0][i] = MAP_FIELD.BORDER;
            environmentState[MAP_HEIGHT-1][i] = MAP_FIELD.BORDER;
        }
        for(int j=0; j<MAP_HEIGHT; j++){
            environmentState[j][MAP_WIDTH-1] = MAP_FIELD.BORDER;
            environmentState[j][0] = MAP_FIELD.BORDER;
        }
    }

    /**
     * Generates given number of random prizes.
     * @param numberOfPrizes number of random prizes
     */
    private void generateRandomPrizes(int numberOfPrizes) {
        for (int i = 0; i < numberOfPrizes; i++) {

            int randomX = ThreadLocalRandom.current().nextInt(1, MAP_WIDTH - 1);
            int randomY = ThreadLocalRandom.current().nextInt(1, MAP_HEIGHT - 1);

            if(environmentState[randomX][randomY] != MAP_FIELD.EMPTY){
                i--;
                continue;
            }

            prizeList.add(new Square(randomX, randomY, 1, 1));

            environmentState[randomX][randomY] = MAP_FIELD.PRIZE;
        }

    }

    /**
     * Generates given number of random obstacles.
     * @param numberOfBoxes number of random obstacles to generate
     */
    private void generateRandomBoxes(int numberOfBoxes) {
//        RANDOM MAP
        for (int i = 0; i < numberOfBoxes; i++) {
            int randomWidth = ThreadLocalRandom.current().nextInt(1, 5);
            int randomHeight = ThreadLocalRandom.current().nextInt(1, 5);

            int randomX = ThreadLocalRandom.current().nextInt(1, MAP_WIDTH - randomWidth);
            int randomY = ThreadLocalRandom.current().nextInt(1, MAP_HEIGHT - randomHeight);

            for (int j = 0; j < randomWidth; j++) {
                for (int k = 0; k < randomHeight; k++) {
                    if (environmentState[j + randomX][k + randomY] != MAP_FIELD.EMPTY) {
                        i--;
                        continue;
                    }
                }
            }

            obstacleList.add(new Square(randomX, randomY, randomWidth, randomHeight));

            for (int j = 0; j < randomWidth; j++) {
                for (int k = 0; k < randomHeight; k++) {
                    environmentState[j + randomX][k + randomY] = MAP_FIELD.OBSTACLE;
                }
            }
        }
    }

    /**
     * Initializes the list of possible states.
     */
    public void initializePossibleStatesList(){

//        brak przeszkód w okolicy robota - 1 stan
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

//        jedna przeszkoda w okolicy robota - 4 stany
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

//        dwie przeszkody w okolicy robota - 6 stanów
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

//        trzy przeszkody w okolicy robota - 4 stany
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

//        cztery przeszkoda w okolicy robota - 1 stan
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

//        granica, brak przeszkód - 4 stany
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

//        dwie granice, brak przeszkód
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.BORDER,null}
        }));

//        jedna granica, jedna przeszkoda
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

//        nagroda

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.PRIZE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.EMPTY,null}
        }));

//        nagroda, 1 granica

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.PRIZE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.PRIZE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.PRIZE,null}
        }));

//        2 przeszkody, 1 granica
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.OBSTACLE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.OBSTACLE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.BORDER,null}
        }));

//        3 przeszkody, 1 granica
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.OBSTACLE},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.OBSTACLE,null},
                {MAP_FIELD.OBSTACLE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.OBSTACLE,null}
        }));

//        nagroda, 2 granice
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.PRIZE,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.BORDER,null}
        }));


        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.PRIZE,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.BORDER,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.EMPTY},
                {null,MAP_FIELD.PRIZE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.PRIZE,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.BORDER},
                {null,MAP_FIELD.PRIZE,null}
        }));

        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.EMPTY,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.BORDER,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.BORDER,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.EMPTY,null}
        }));
        possibleStatesList.add(State.with(new Environment.MAP_FIELD[][]{
                {null,MAP_FIELD.EMPTY,null},
                {MAP_FIELD.BORDER,null,MAP_FIELD.PRIZE},
                {null,MAP_FIELD.BORDER,null}
        }));

    }
}
