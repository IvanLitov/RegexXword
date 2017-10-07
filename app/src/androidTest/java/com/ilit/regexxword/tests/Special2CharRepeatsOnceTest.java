package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.Special2CharRepeatsOnce;

public class Special2CharRepeatsOnceTest extends AndroidTestCase
{

	public void testSuccess()
	{
		Special2CharRepeatsOnce _sp = new Special2CharRepeatsOnce();
		assertTrue(_sp.buildHint(new String[] { "BLAHSOMEAHHERE" }));
	}
	
	public void testSuccessWithNoOutsideChars()
	{
		Special2CharRepeatsOnce _sp = new Special2CharRepeatsOnce();
		assertTrue(_sp.buildHint(new String[] { "AHSOMEAH" }));
	}
	
	public void testFailNoRepeatingChars()
	{
		Special2CharRepeatsOnce _sp = new Special2CharRepeatsOnce();
		assertFalse(_sp.buildHint(new String[] { "BLAHSOMEAZHERE" }));
	}

	public void testFailNoGapBetweenRepeats()
	{
		Special2CharRepeatsOnce _sp = new Special2CharRepeatsOnce();
		assertFalse(_sp.buildHint(new String[] { "BLAHAHHERE" }));
	}
}
