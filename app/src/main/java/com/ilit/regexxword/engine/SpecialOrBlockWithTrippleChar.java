package com.ilit.regexxword.engine;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialOrBlockWithTrippleChar extends SpecialOrBlock
{
	@Override
	public boolean buildHint(String[] str)
	{
		Matcher _matcher = Pattern.compile("(.*)(.)\\2\\2(.*)").matcher(str[0]);
		
		if (_matcher.matches())
		{
			HashSet<String> _hash = new HashSet<String>();
			String _char = _matcher.group(2);
			_hash.add(_char + _char + _char);
			splitString(_matcher.group(1), _hash);
			splitString(_matcher.group(3), _hash);
			
			str[0] = convertHashToHint(_hash);
			return true;
		}
		else
		{
			return false;
		}
	}
}
