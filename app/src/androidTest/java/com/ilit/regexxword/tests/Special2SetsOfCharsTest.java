package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.BasicLeaveAsIs;
import com.ilit.regexxword.engine.Special2SetsOfChars;

public class Special2SetsOfCharsTest extends AndroidTestCase
{

	public void testSuccessThreeChars()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "ABCDDDEZZZZZZZZZZZZ" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("ABCD*EZ*"));
	}

	public void testSuccessFourCharsNoGap()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "ABCEEEEEEEEDDDD" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("ABCE*D*"));
	}

	public void testSuccessBothGroupsAtStart()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "AAAAAABBBBBBBBBEDDDD" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("A*B*EDDDD"));
	}

	public void testSuccessFiveCharsAtStartAndEnd()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DDDDDDABCHHHHHHHH" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("D*ABCH*"));
	}

	public void testSuccessFiveCharsInMiddle()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "XYDDDDDAMMMMMMMBC" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("XYD*AM*BC"));
	}

	public void testFail()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DZDDABC" };
		assertFalse(_sp.buildHint(_input));
	}

	public void testFailOneGroup()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DZDDDDDDABC" };
		assertFalse(_sp.buildHint(_input));
	}

	public void testFailOneGroupWithSecondTooShort()
	{
		Special2SetsOfChars _sp = new Special2SetsOfChars();
		_sp.setBasicHint(new BasicLeaveAsIs());
		String[] _input = new String[] { "DZDDDDDDABCUU" };
		assertFalse(_sp.buildHint(_input));
	}
}
