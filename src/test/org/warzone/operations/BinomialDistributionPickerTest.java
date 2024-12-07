package org.warzone.operations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the BinomialDistributionPicker utility.
 */
public class BinomialDistributionPickerTest {
    /** Total number of tests to run for distribution checking. */
    private static int NUM_TESTS;
    /** Total number of armies used in tests. */
    private static int NUM_ARMIES;
    /** Probability of success for the binomial distribution. */
    private static double SUCCESS_PROBABILITY;

    /**
     * Sets up the test environment with default parameters.
     */
    @BeforeAll
    public static void setUp() {
        NUM_TESTS = 10000;
        NUM_ARMIES = 10;
        SUCCESS_PROBABILITY = 0.6;

    }

    /**
     * Test to verify the distribution behavior of the BinomialDistributionPicker.
     * It will generate a distribution based on NUM_TESTS, and then assert that the mean
     * of this distribution is approximately equal to the expected mean (NUM_ARMIES * SUCCESS_PROBABILITY).
     */
    @Test
    public void testMean() {
        int[] l_distribution = new int[NUM_ARMIES + 1];
        for (int l_i = 0; l_i < NUM_TESTS; l_i++) {
            int numKills = BinomialDistributionPicker.getNumberOfKills(NUM_ARMIES, SUCCESS_PROBABILITY);
            l_distribution[numKills]++;
        }
        double sum = 0;
        for (int i = 0; i < l_distribution.length; i++) {
            sum += i * l_distribution[i];
        }
        System.out.println(sum / NUM_TESTS);
        assertEquals(NUM_ARMIES * SUCCESS_PROBABILITY, sum / NUM_TESTS, 0.1);

    }
}