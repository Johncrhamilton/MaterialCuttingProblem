package model;

import java.util.Random;

public class ModelConstants {

	//General Constants
	public static final boolean LIMITED_ITERATIONS = true;
	public static final int ITERATION_LIMIT = 100;
	public static final long TIME_LIMIT = 5000;
	
	public static final Random RANDOM = new Random();
	public static final int RANDOM_CUT_ACTIVITY_MAX_ATTEMPTS = 5;
	
	//Local Search Constants
	public static final int NEIGHBOURHOOD_SIZE = 100;
	
	//General Evolution Algorithm Constants
	public static final int POPULATION_SIZE = 100;
	public static final int OFFSPRING_POPULATION_SIZE = 100;
	public static final int TOURNAMENT_SELECTIVE_SAMPLE = 5;
	public static final boolean ELITISM = true;
	
	//Evolution Algorithm Constants for Baseline
	public static final double BASE_RECOMBINATION_PROBABILITIY = 1;
	public static final double BASE_MUTATION_PROBABILITY = 0.75;
	
	//Evolution Algorithm Constants for my Implementation
	public static final int CENTRE_OF_MASS_NEIGHBOURHOOD_SIZE = 100;
	public static final double CENTRE_OF_MASS_POSITIVE_TO_NEGATIVE_DISTRIBUTION = 0.5; 
	public static final double RECOMBINATION_PROBABILITIY = 0.95;
	public static final double MUTATION_PROBABILITY = 0.05;
}
