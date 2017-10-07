package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.Special1CharRepeatsTwice;

public class Special1CharRepeatsTwiceTest extends AndroidTestCase
{

	public void testSuccess()
	{
		Special1CharRepeatsTwice _sp = new Special1CharRepeatsTwice();
		assertTrue(_sp.buildHint(new String[] { "ABRACADBRZ" }));
	}
	
	public void testFailNothreeChars()
	{
		Special1CharRepeatsTwice _sp = new Special1CharRepeatsTwice();
		assertFalse(_sp.buildHint(new String[] { "ABRHCDDBRZ" }));
	}
	
	public void testFailNoGaps1()
	{
		Special1CharRepeatsTwice _sp = new Special1CharRepeatsTwice();
		assertFalse(_sp.buildHint(new String[] { "ABRAADBRZ" }));
	}
	
	public void testFailNoGaps2()
	{
		Special1CharRepeatsTwice _sp = new Special1CharRepeatsTwice();
		assertFalse(_sp.buildHint(new String[] { "ABRAACDDBRZ" }));
	}

}
