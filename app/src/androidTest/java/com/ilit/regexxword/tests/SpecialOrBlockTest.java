package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.SpecialOrBlock;

public class SpecialOrBlockTest extends AndroidTestCase
{

	public void testSixChars()
	{
		SpecialOrBlock _sp = new SpecialOrBlock();
		String[] _input = new String[] { "ABCDEF" };
		_sp.buildHint(_input);
		assertTrue(_input[0].equals("(ABC|DEF)*"));
	}

	public void testSevenChars()
	{
		SpecialOrBlock _sp = new SpecialOrBlock();
		String[] _input = new String[] { "ABCDEFG" };
		_sp.buildHint(_input);
		assertTrue(_input[0].equals("(A|BCD|EFG)*"));
	}

	public void testEightChars()
	{
		SpecialOrBlock _sp = new SpecialOrBlock();
		String[] _input = new String[] { "ABCDEFGH" };
		_sp.buildHint(_input);
		assertTrue(_input[0].equals("(AB|CDE|FGH)*"));
	}
}
