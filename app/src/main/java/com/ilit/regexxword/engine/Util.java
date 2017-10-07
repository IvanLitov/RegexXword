package com.ilit.regexxword.engine;



public class Util
{
	/**
	 * Randomly choose an integer between 0 and max-1.
	 */
	public static int random(int max)
	{
		// The * 0.999999 ensures the random can never return 1 as result, so Math.floor
		// will always give a value between 0 and max -1, which is ideal for picking items
		// out of an array of size max.
		return (int) Math.floor(max * (Math.random() * 0.999999));
	}
	
	/**
	 * Validates if the input string contains exactly n unique characters and returns these
	 * characters as an array. The "n" is determined by the size of the result array.
	 * @param input = input string to validate
	 * @param result = char array to return containing the unique characters. The size of this
	 * array determines how many characters to look for.
	 * @return
	 */
	public static boolean isNChars(String input, char[] result)
	{
		char[] _inputArray = input.toCharArray();
		
		int i = 0;	// This is the index in the result array where the char is to be posted.
		
		for (int c = 0; c < _inputArray.length; c++)
		{
			boolean _charAlreadyInResult = false;
			
			for (int r = 0; r < i; r++)
			{
				if (result[r] != '\0' && result[r] == _inputArray[c])
				{
					_charAlreadyInResult = true;
					break;
				}
			}
			
			if (!_charAlreadyInResult)
			{
				if (i >= result.length)
				{
					return false;
				}
				else
				{
					result[i] = _inputArray[c];
					i++;
				}
			}
		}
		
		return (i == result.length);
	}
}
