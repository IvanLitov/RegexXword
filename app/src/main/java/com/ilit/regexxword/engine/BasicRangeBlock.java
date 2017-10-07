package com.ilit.regexxword.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class BasicRangeBlock extends BasicHintBase
{
	@Override
	public String buildHint(String str)
	{
		// Ignore zero-length strings
		if (str.length() == 0)
			return str;
		
		// Find unique characters
		ArrayList<Character> _list = new ArrayList<Character>();
		for (Character c : str.toCharArray())
			_list.add(c);
		
		HashSet<Character> _hash = new HashSet<Character>();
		_hash.addAll(_list);
		_list.clear();
		_list.addAll(_hash);		
		
		// Add 1-3 fake characters
		for (Character c : CharEngine.getUniqueRandomArray(1 + Util.random(2), str.toCharArray()))
			_list.add(c);
			
		// Sort the list in alphabetical order	
		Collections.sort(_list);
		
		// Return the result as a string
		StringBuilder _builder = new StringBuilder("[");
		for (char c : _list)
			_builder.append(c);
		
		_builder.append("]");
		_builder.append(str.length() == 1 ? "" : "+");
		
		return _builder.toString();
	}
}
