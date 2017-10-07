package com.ilit.regexxword.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The string has two characters which repeat in reverse. 
 * Converts to a pattern like .*(.)(.).*\2\1.*
 */
public class Special2CharsInReverse extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		Matcher _matcher = Pattern.compile("(.*)(.)(.)(.+)\\3\\2(.*)").matcher(str[0]);
		
		if (_matcher.matches())
		{
			BasicDistributor _dist = new BasicDistributor(false);
			StringBuilder _builder = new StringBuilder("");
			str[0] = _builder.append(_dist.buildHint(_matcher.group(1)))
							 .append("(.)(.)")
							 .append(_dist.buildHint(_matcher.group(4)))
							 .append("\\2\\1")
							 .append(_dist.buildHint(_matcher.group(5)))
							 .toString();
			return true;
		}
		else
		{
			return false;
		}
	}
}
