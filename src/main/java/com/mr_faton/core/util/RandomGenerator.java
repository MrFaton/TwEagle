package com.mr_faton.core.util;

import java.util.Random;

public class RandomGenerator {
    public static int getNumberFromZeroToRequirement(int max_number) {
//        return new Random().nextInt(++max_number);
        return getNumber(0, max_number);
    }

    public static int getNumber(int min_number, int max_number) {
        return min_number + (int) (Math.random() * ((max_number - min_number) + 1));
    }
}
