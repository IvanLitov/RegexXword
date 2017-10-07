package com.ilit.regexxword.tests;

import android.test.AndroidTestCase;

import com.ilit.regexxword.engine.SpecialOrBlockWithTrippleChar;

public class SpecialOrBlockWithTrippleCharTest extends AndroidTestCase
{
	public void testSuccess()
	{
		SpecialOrBlockWithTrippleChar _sp = new SpecialOrBlockWithTrippleChar();
		String[] _input = new String[] { "OEMCTDKLGGGTPFXE" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("(DKL|FXE|GGG|MCT|OE|TP)*"));
	}

	public void testFail()
	{
		SpecialOrBlockWithTrippleChar _sp = new SpecialOrBlockWithTrippleChar();
		String[] _input = new String[] { "OEMCTDKLGGTPFXE" };
		assertFalse(_sp.buildHint(_input));
	}
	
	public void testSuccessMatchAtBeginning()
	{
		SpecialOrBlockWithTrippleChar _sp = new SpecialOrBlockWithTrippleChar();
		String[] _input = new String[] { "GGGTPFXE" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("(FXE|GGG|TP)*"));
	}

	public void testSuccessMatchAtEnd()
	{
		SpecialOrBlockWithTrippleChar _sp = new SpecialOrBlockWithTrippleChar();
		String[] _input = new String[] { "OEMCTDKLGGG" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("(DKL|GGG|MCT|OE)*"));
	}

	public void testSuccessOneCharOutside()
	{
		SpecialOrBlockWithTrippleChar _sp = new SpecialOrBlockWithTrippleChar();
		String[] _input = new String[] { "GGGE" };
		assertTrue(_sp.buildHint(_input));
		assertTrue(_input[0].equals("(E|GGG)*"));
	}
}
