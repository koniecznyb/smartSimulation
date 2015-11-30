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
    private static final int NUMBER_OF_BOXES = 20, NUMBER_OF_PRIZES = 0, MAP_WIDTH = 50, MAP_HEIGHT = 50;
    @Getter private MAP_FIELD[][] environmentState;
    @Getter private List<Square> obstacleList = new ArrayList<>(), prizeList = new ArrayList<>();
    @Getter private static List<State> possibleStatesList = new ArrayList<>();


    private Environment(MAP_FIELD[][] environmentState) {
        this.environmentState = environmentState;
    }

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

    public void initializeMap() {

        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                environmentState[i][j] = Environment.MAP_FIELD.EMPTY;
            }
        }
    }

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

    }

    public void populateEnvironment(){
        generateBorders();
        generateBoxes(NUMBER_OF_BOXES);
        generatePrizes(NUMBER_OF_PRIZES);

    }

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
