package edu.school21.game.utils.types;

import edu.school21.utils.RandomGenerator;

import java.util.List;

public enum ClusterType {
    UNIVERSE, PROGRESS, GENOM, EVOLUTION, SINGULARITY, ETERNITY;

    private static final List<ClusterType> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();

    public static ClusterType getRandom()  {
        return VALUES.get(RandomGenerator.generate(SIZE));
    }
}
