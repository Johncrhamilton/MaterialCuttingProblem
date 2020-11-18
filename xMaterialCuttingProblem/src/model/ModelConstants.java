package model;

import java.util.Random;

public class ModelConstants {

	//General Constants
	public static final boolean LIMITED_ITERATIONS = true;	
	public static final int ITERATION_LIMIT = 100;
	public static final long TIME_LIMIT = 2000;
	public static final Random random = new Random();
	
	//Evolution Algorithm Constants
	public static final double RECOMBINATION_PROBABILITIY = 1.0;
	public static final double MUTATION_PROBABILITY = 0.75;
	public static final int POPULATION_SIZE = 100;
	public static final int TOURNAMENT_SELECTIVE_SAMPLE = 3;
	public static final boolean ELITISM = true;
	
}
