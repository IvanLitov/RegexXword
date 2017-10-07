package com.ilit.regexxword.engine;

import java.util.ArrayList;

import com.ilit.regexxword.bo.Const;

/**
 * Singleton that is used for generating random characters
 */
public class CharEngine
{
	private final ArrayList<Character> _population;
	private static CharEngine _instance;
	private final static char[] _allowedChars = new char[] 
	{
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' 
	};
	
	
	private CharEngine()
	{
		// Put all possible chars in collection
		_population = new ArrayList<Character>();
		for (char s : _allowedChars) _population.add(s);
		
		// Remove some of the letters
		int _expectedSize = (int)Math.round(_population.size() * Const.ALPHA);
		while (_population.size() > _expectedSize)
		{
			_population.remove(Util.random(_population.size()));
		}
	}
	
	
	public static char[] getAllowedChars()
	{
		return _allowedChars;
	}
	

	/**
	 * Gets an array of random characters of a given size excluding chars in the exclude list.
	 * This version of the method ensures the characters in the returned array are unique.
	 * @param size = size of the array to return.
	 * @param exclude = list of characters to exclude
	 * @return
	 */
	public static char[] getUniqueRandomArray(int size, char[] exclude)
	{
		// Get the population to pull characters from
		if (_instance == null)
			_instance = new CharEngine();
		
		ArrayList<Character> _popn = new ArrayList<Character>();
		
		for (char c : _instance._population)
			_popn.add(c);
		
		for (char c : exclude)
		{
			for (int i = 0; i < _popn.size(); i++)
			{
				if (c == _popn.get(i))
				{
					_popn.remove(i);
					break;
				}
			}
		}

		// Draw the characters from the population and remove to ensure chars are not repeated.
		char[] _out = new char[size];
		
		for (int i = 0; i < size; i++)
		{
			int _ind = Util.random(_popn.size());
			_out[i] = _popn.get(_ind);
			_popn.remove(_ind);
			
			if (_popn.size() == 0)
				break;
		}
		
		return _out;
	}

	
	/**
	 * Generates a single random character
	 */
	public static char getRandomChar()
	{
		// Get the population to pull characters from
		if (_instance == null)
			_instance = new CharEngine();

		ArrayList<Character> _popn = _instance._population;
		return _popn.get(Util.random(_popn.size()));
	}
}
