package org.redi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by redi on 2015-10-21.
 */
public enum Action {

    MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT;

    private static final List<Action> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Action randomAction()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
