package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.Special3CharsOnly;

public class Special3CharsOnlyTest extends AndroidTestCase
{
	public void testSuccessSingleChar()
	{
		Special3CharsOnly _sp = new Special3CharsOnly();
		String[] _input = new String[] { "SSIQQ" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("S*I*Q*"));
	}
	
	public void testSuccessMultipleChars()
	{
		Special3CharsOnly _sp = new Special3CharsOnly();
		String[] _input = new String[] { "DDDMMMMZZZZZ" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("D*M*Z*"));
	}
	
	public void testFailGap()
	{
		Special3CharsOnly _sp = new Special3CharsOnly();
		assertFalse(_sp.buildHint(new String[] { "DDDAMZZZZZ" }));
	}
	
	public void testFailTwoGroupsOfChars()
	{
		Special3CharsOnly _sp = new Special3CharsOnly();
		assertFalse(_sp.buildHint(new String[] { "AAAAAABBBB" }));
	}
	
	public void testFailFourGroupsWithThreeChars()
	{
		Special3CharsOnly _sp = new Special3CharsOnly();
		assertFalse(_sp.buildHint(new String[] { "AAAAAABBBBACCCCC" }));
	}

}
