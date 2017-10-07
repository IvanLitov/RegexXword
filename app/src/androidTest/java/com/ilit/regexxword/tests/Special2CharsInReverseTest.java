package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.Special2CharsInReverse;

public class Special2CharsInReverseTest extends AndroidTestCase
{

	public void testSuccess()
	{
		Special2CharsInReverse _sp = new Special2CharsInReverse();
		assertTrue(_sp.buildHint(new String[] { "ZABAZ" }));
	}

	public void testFailNoMatch()
	{
		Special2CharsInReverse _sp = new Special2CharsInReverse();
		assertFalse(_sp.buildHint(new String[] { "ZAABZ" }));
	}

	public void testFailNoGap()
	{
		Special2CharsInReverse _sp = new Special2CharsInReverse();
		assertFalse(_sp.buildHint(new String[] { "ZAAZ" }));
	}

}
