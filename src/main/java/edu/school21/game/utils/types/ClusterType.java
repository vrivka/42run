package edu.school21.game.utils.types;

import edu.school21.utils.RandomGenerator;

public enum ClusterType {
    UNIVERSE, PROGRESS, GENOM, EVOLUTION, SINGULARITY, ETERNITY;

    public static ClusterType getRandom() {
        return switch (RandomGenerator.generate(0, 6)) {
            case 0 -> UNIVERSE;
            case 1 -> PROGRESS;
            case 2 -> GENOM;
            case 3 -> EVOLUTION;
            case 4 -> SINGULARITY;
            case 5 -> ETERNITY;
            default -> null;
        };
    }
}
