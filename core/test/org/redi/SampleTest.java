package org.redi;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.Arrays;

/**
 * Created by Bartłomiej Konieczny on 2015-11-29.
 */
public class SampleTest {
    @Before
    public void setUp() throws Exception {
        Environment environment = Environment.getInstance();
        environment.initializeMap();
        environment.populateEnvironment();
    }

    @Test public void shouldReturnAgentEnvironment() {
        Environment.MAP_FIELD [][] environmentMap = Environment.getInstance().getEnvironmentState();
        int agentPositionX = 20;
        int agentPositionY = 20;

        Environment.MAP_FIELD [][] agentSurroundings = new Environment.MAP_FIELD[3][3];

        for(int i = 0, y = agentPositionY-1; i < 3; i++, y++){
            agentSurroundings[i] = Arrays.copyOfRange(environmentMap[y], agentPositionX-1, agentPositionX+2);
        }

        System.out.println(agentSurroundings);
    }

}
