package com.ilit.regexxword.engine;

import java.util.regex.Pattern;

/**
 * The string is made up of three groups of repeating characters. 
 * Converted to a pattern like A*B*C*
 */
public class Special3CharsOnly extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		char[] _chars = new char[3];
		if (!Util.isNChars(str[0], _chars))
			return false;

		if (Pattern.matches("(.)\\1*(.)\\2*(.)\\3*", str[0]))
		{
			str[0] = new StringBuilder("")
						.append(_chars[0]).append("*")
						.append(_chars[1]).append("*")
						.append(_chars[2]).append("*")
						.toString();
			return true;
		}
		else 
		{
			return false;
		}
	}
}
