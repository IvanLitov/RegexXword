package com.ilit.regexxword.engine;

/**
 * This category of patterns ("Specials") is triggered if a) the input
 * string contains the pattern and b) the randomly generated number is above the threshold
 * for the pattern. Default threshold is 0.
 */
public abstract class SpecialHintBase
{
	private float _triggerProbability = 1;
	
	/**
	 * This methods sets the probability with which the particular pattern will be triggered.
	 * The method returns the class itself so that it can be used in constructors.
	 * @param prob = probability of triggering this pattern.
	 * @return
	 */
	public SpecialHintBase setTriggerProbability(float prob)
	{
		_triggerProbability = prob;
		return this;
	}
	
	/**
	 * Custom method for building hints, which is implemented in the
	 * concrete classes 
	 * @param str = input/output. Supply input string, return output hint. The param must be an array
	 * so that it is "effectively" handled ByRef.
	 * @return returns true if the special pattern was triggered.
	 */
	public abstract boolean buildHint(String[] str);
	
	public boolean buildHintWithProbability(String[] input)
	{
		if (Math.random() > _triggerProbability)
			return false;
		
		return this.buildHint(input);
	}
}
