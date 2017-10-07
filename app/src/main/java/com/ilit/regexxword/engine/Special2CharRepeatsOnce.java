package com.ilit.regexxword.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The string has one character repeating twice. Converts to a pattern like .*(.).*\1.*\1.*
 */
public class Special2CharRepeatsOnce extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		Matcher _matcher = Pattern.compile("(.*)(..)(.+)\\2(.*)").matcher(str[0]);
		
		if (_matcher.matches())
		{
			BasicDistributor _dist = new BasicDistributor(false);
			StringBuilder _builder = new StringBuilder("");
			str[0] = _builder.append(_dist.buildHint(_matcher.group(1)))
							 .append("(..)")
							 .append(_dist.buildHint(_matcher.group(3)))
							 .append("\\1")
							 .append(_dist.buildHint(_matcher.group(4)))
							 .toString();
			return true;
		}
		else
		{
			return false;
		}
	}
}
