package com.ilit.regexxword.engine;

import java.util.ArrayList;
import java.util.Collections;

public class BasicNotRangeBlock extends BasicHintBase
{
	@Override
	public String buildHint(String str)
	{
		// Ignore zero-length strings
		if (str.length() == 0)
			return str;

		
		// Add 2-5 not characters
		ArrayList<Character> _list = new ArrayList<Character>();
		for (Character c : CharEngine.getUniqueRandomArray(2 + Util.random(3), str.toCharArray()))
			_list.add(c);
			
		// Sort the list in alphabetical order	
		Collections.sort(_list);
		
		// Return the result as a string
		StringBuilder _builder = new StringBuilder("[^");
		for (char c : _list)
			_builder.append(c);
		
		_builder.append("]");
		_builder.append(str.length() == 1 ? "" : "+");
		
		return _builder.toString();
	}
}
