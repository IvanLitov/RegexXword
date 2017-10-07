package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.BasicLeaveAsIs;
import com.ilit.regexxword.engine.Special1CharRepeats3PlusTimes;

public class Special1CharRepeats3PlusTimesTest extends AndroidTestCase
{
	public void testSuccessThreeChars()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "ABCDDDE" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("ABCD*E"));
	}

	public void testSuccessFourChars()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "ABCDDDD" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("ABCD*"));
	}

	public void testSuccessFiveCharsAtStart()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DDDDDDABC" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("D*ABC"));
	}

	public void testSuccessFiveCharsAtEnd()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "ABCDDDDDD" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("ABCD*"));
	}

	public void testSuccessFiveCharsInMiddle()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "XYDDDDDABC" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("XYD*ABC"));
	}

	public void testFail()
	{
		Special1CharRepeats3PlusTimes _sp = new Special1CharRepeats3PlusTimes();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DZDDABC" };
		assertFalse(_sp.buildHint(_input));
	}
}
