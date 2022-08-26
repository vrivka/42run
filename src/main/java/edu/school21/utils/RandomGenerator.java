package edu.school21.utils;

import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();

    public static int generate(int from, int to) {
        return random.nextInt(from, to);
    }

    public static float generate(float from, float to) {
        return random.nextFloat(from, to);
    }

    public static float generateOne(float one, float two) {
        if (random.nextBoolean()) {
            return one;
        } else {
            return two;
        }
    }
}
