package com.ilit.regexxword.engine;

/**
 * This category of patterns ("Basics") always returns a hint. These are used when either
 * a) no special pattern was triggered on the input string or b) the special pattern only
 * obfuscates part of the string and the remaining parts need to be obfuscated using
 * basic patterns.
 */
public abstract class BasicHintBase
{
	public abstract String buildHint(String str);
	
	/**
	 * Allows certain basic patterns to be omitted if the input string is longer
	 * than the threshold length.
	 */
	public int getMaxStringSize()
	{
		return 1000;
	}
}
