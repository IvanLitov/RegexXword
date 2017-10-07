package com.ilit.regexxword.bo;

/**
 * Holds various global constants
 */
public class Const
{
	/**
	 * Fraction of alphabet to draw random chars from
	 */
	public static final float ALPHA = 0.5f;
	
	/**
	 * Probability of random character repeating
	 */
	public static final float REPEAT = 0.5f;

	/**
	 * Max proportion of non-unique cells allowed in a map
	 */
	public static final float MAX_BAD_CELLS = 0.2f;
	
	/**
	 * Map buffer to allow for status bar at top
	 */
	public static final float MAP_BUFFER = 0.05f;
	
	/**
	 * Probability of SpecialOrBlock triggering
	 */
	public static final float PROB_OR_BLOCK = 0.5f;

	/**
	 * Probability of SpecialOrBlockForPart triggering
	 */
	public static final float PROB_OR_BLOCK_PART = 0.5f;
}

