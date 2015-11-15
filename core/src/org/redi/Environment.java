package org.redi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by redi on 2015-10-17.
 */
public class Environment {

    private static Environment instance = null;
    private static final int NUMBER_OF_BOXES = 15, NUMBER_OF_PRIZES = 10, MAP_WIDTH = 50, MAP_HEIGHT = 50;
    @Getter private MAP_FIELD[][] environmentState;
    @Getter private List<Square> obstacleList = new ArrayList<>(), prizeList = new ArrayList<>();



    private Environment(MAP_FIELD[][] environmentState) {
        this.environmentState = environmentState;
    }

    public static Environment getInstance(){
        if(instance == null) {
            instance = new Environment(new MAP_FIELD[MAP_WIDTH][MAP_HEIGHT]);
        }
        return instance;
    }

    public int respond(Action a, int agentPositionX, int agentPositionY){

        return 2;
    }


    public enum MAP_FIELD {

        EMPTY(0), OBSTACLE(1), PRIZE(2);

        private int numValue;

        MAP_FIELD(int value) {
            this.numValue = value;
        }

        public int getNumValue() {
            return numValue;
        }
    }

    public void initializeMap() {

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                environmentState[i][j] = Environment.MAP_FIELD.EMPTY;
            }
        }
    }

    public void populateEnvironment(){
        generateBoxes(NUMBER_OF_BOXES);
        generatePrizes(NUMBER_OF_PRIZES);

    }

    private void generatePrizes(int numberOfPrizes) {
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

    private void generateBoxes(int numberOfBoxes) {
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


}
