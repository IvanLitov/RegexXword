package com.ilit.regexxword.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SpecialOrBlock extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		HashSet<String> _hash = new HashSet<String>();
		splitString(str[0], _hash);
		str[0] = convertHashToHint(_hash);
		return true;
	}
	
	// Breaks the string up into chunks of three chars
	protected static void splitString(String input, HashSet<String> hash)
	{
		int _length = input.length();
		if (_length > 0)
		{
			if (_length <= 3)
			{
				hash.add(input);
			}
			else 
			{
				int _residual = _length % 3;
				if (_residual == 0)
				{
					int i = 0;
					while (i < input.length())
					{
						hash.add(input.substring(i, i + 3));
						i += 3;
					}
				}
				else
				{
					hash.add(input.substring(0, _residual));
					splitString(input.substring(_residual), hash);
				}
			}
		}
	}
	
	protected static String convertHashToHint(HashSet<String> hash)
	{
		
		ArrayList<String> _list = new ArrayList<String>();
		_list.addAll(hash);
		
		Collections.sort(_list);
		
		StringBuilder _builder = new StringBuilder("(");
		_builder.append(_list.get(0));
		
		for (int i = 1; i < _list.size(); i++)
			_builder.append("|").append(_list.get(i));
		
		_builder.append(")*");
		
		return _builder.toString();
	}
}
