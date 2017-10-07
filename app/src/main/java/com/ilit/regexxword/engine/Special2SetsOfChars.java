package com.ilit.regexxword.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Special2SetsOfChars extends SpecialHintBase
{
	private BasicHintBase _basicHint = new BasicDistributor();
	
	@Override
	public boolean buildHint(String[] str)
	{
		// Need to stick the question mark into the first and last group in case the
		// repeating char is at the beginning or end of the string.
		Matcher _matcher = Pattern.compile("(.*?)(.)\\2{2,}(.*?)(.)\\4{2,}(.*?)").matcher(str[0]);
		
		if (_matcher.matches())
		{
			// Check that the two groups of repeating characters are not the same
			if (_matcher.group(2).equalsIgnoreCase(_matcher.group(4)))
			{
				// If the two strings are the same, check there is a separating character in the middle
				if (_matcher.group(3).length() == 0 || _matcher.group(3).equalsIgnoreCase(_matcher.group(2)))
				{
					return false;
				}
			}
			
			StringBuilder _builder = new StringBuilder("");
			str[0] = _builder.append(_basicHint.buildHint(_matcher.group(1)))
							 .append(_matcher.group(2))
							 .append("*")
							 .append(_matcher.group(3))
							 .append(_matcher.group(4))
							 .append("*")
							 .append(_basicHint.buildHint(_matcher.group(5)))
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
