package rpg.utils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating various types of random numbers and values
 * with thread-safe implementation
 */
public final class RandomGenerator {
    private static final Random random = ThreadLocalRandom.current();
    
    // Private constructor to prevent instantiation
    private RandomGenerator() {}

    /**
     * Generates a random integer between min (inclusive) and max (inclusive)
     */
    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Generates a random double between 0 (inclusive) and 1 (exclusive)
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * Generates a random boolean with specified probability
     * @param chanceOfTrue Probability between 0.0 (never true) and 1.0 (always true)
     */
    public static boolean randomBoolean(double chanceOfTrue) {
        return random.nextDouble() < chanceOfTrue;
    }

    /**
     * Selects a random element from a list
     */
    public static <T> T randomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Selects a random element from an array
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        return array[random.nextInt(array.length)];
    }

    /**
     * Generates a random value following a normal distribution
     * @param mean The mean of the distribution
     * @param stdDev The standard deviation
     */
    public static double randomGaussian(double mean, double stdDev) {
        return mean + random.nextGaussian() * stdDev;
    }

    /**
     * Randomly shuffles the elements of an array
     */
    public static void shuffleArray(Object[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Generates a random percentage value (0-100)
     */
    public static int randomPercentage() {
        return randomInt(0, 100);
    }

    /**
     * Sets a specific seed for deterministic random generation (useful for testing)
     */
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    /**
     * Gets a random enum value from the specified enum class
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }
}
