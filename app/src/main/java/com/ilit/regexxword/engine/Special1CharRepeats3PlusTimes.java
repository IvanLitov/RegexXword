package com.ilit.regexxword.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Special1CharRepeats3PlusTimes extends SpecialHintBase
{
	private BasicHintBase _basicHint = new BasicDistributor();
	
	@Override
	public boolean buildHint(String[] str)
	{
		// Need to stick the question mark into the first and last group in case the
		// repeating char is at the beginning or end of the string.
		Matcher _matcher = Pattern.compile("(.*?)(.)\\2{2,}(.*?)").matcher(str[0]);
		
		if (_matcher.matches())
		{
			StringBuilder _builder = new StringBuilder("");
			str[0] = _builder.append(_basicHint.buildHint(_matcher.group(1)))
							 .append(_matcher.group(2))
							 .append("*")
							 .append(_basicHint.buildHint(_matcher.group(3)))
							 .toString();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This method is only used for unit testing.
	 */
	public void setBasicHint(BasicHintBase bh)
	{
		_basicHint = bh;
	}
}
