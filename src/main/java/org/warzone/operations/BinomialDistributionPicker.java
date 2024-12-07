package org.warzone.operations;

import java.util.Random;

/**
 * Utility class for selecting values based on a binomial distribution.
 */
public final class BinomialDistributionPicker {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private BinomialDistributionPicker() {
        // Prevent instantiation
    }

    /**
     * Returns the number of 'kills' based on a binomial distribution.
     *
     * @param p_numArmies            Number of armies.
     * @param p_probabilityOfSuccess Probability of success for each trial.
     * @return An integer representing the number of 'kills'.
     */
    public static int getNumberOfKills(int p_numArmies, double p_probabilityOfSuccess) {
        double[] l_probabilities = binomialDistribution(p_numArmies, p_probabilityOfSuccess);
        return pickNumber(l_probabilities);
    }

    /**
     * Selects a number based on given probabilities.
     *
     * @param p_probabilities An array of probabilities.
     * @return An integer index chosen based on the provided probabilities.
     */
    private static int pickNumber(double[] p_probabilities) {
        Random l_rand = new Random();
        double l_randVal = l_rand.nextDouble();

        double l_cumulativeProb = 0;
        for (int l_index = 0; l_index < p_probabilities.length; l_index++) {
            l_cumulativeProb += p_probabilities[l_index];
            if (l_randVal <= l_cumulativeProb) {
                return l_index;
            }
        }
        return p_probabilities.length - 1;
    }

    /**
     * Computes the binomial distribution for the given number of trials and success probability.
     *
     * @param p_numTrials            Number of trials.
     * @param p_probabilityOfSuccess Probability of success for each trial.
     * @return An array representing the binomial distribution.
     */
    private static double[] binomialDistribution(int p_numTrials, double p_probabilityOfSuccess) {
        double[] l_probabilities = new double[p_numTrials + 1];
        for (int l_k = 0; l_k <= p_numTrials; l_k++) {
            l_probabilities[l_k] = binomialCoefficient(p_numTrials, l_k) * Math.pow(p_probabilityOfSuccess, l_k) * Math.pow(1 - p_probabilityOfSuccess, p_numTrials - l_k);
        }
        return l_probabilities;
    }

    /**
     * Calculates the binomial coefficient for given values of n and k.
     *
     * @param p_n Value n for the binomial coefficient.
     * @param p_k Value k for the binomial coefficient.
     * @return The binomial coefficient for the given values.
     */
    private static double binomialCoefficient(int p_n, int p_k) {
        double l_result = 1;
        for (int l_i = 1; l_i <= p_k; l_i++) {
            l_result *= (p_n - l_i + 1);
            l_result /= l_i;
        }
        return l_result;
    }
}
