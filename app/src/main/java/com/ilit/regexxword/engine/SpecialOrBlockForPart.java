package com.ilit.regexxword.engine;

public class SpecialOrBlockForPart extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		// Split the string into roughly half, allocate the longest part to
		// the or block and the rest to the basic hint builder.

		int _left = (str[0].length() / 2 - 1) + Util.random(3);
		String _part1 = str[0].substring(0, _left);
		String _part2 = str[0].substring(_left, str[0].length());
		
		if (_part1.length() > _part2.length())
		{
			String[] _specialChunk = new String[] { _part1 };
			new SpecialOrBlock().buildHint(_specialChunk);
			_part1 = _specialChunk[0];
			
			_part2 = new BasicDistributor().buildHint(_part2);
		}
		else
		{
			_part1 = new BasicDistributor().buildHint(_part1);

			String[] _specialChunk = new String[] { _part2 };
			new SpecialOrBlock().buildHint(_specialChunk);
			_part2 = _specialChunk[0];
		}
		
		str[0] = _part1 + _part2;
		
		return true;
	}
}
