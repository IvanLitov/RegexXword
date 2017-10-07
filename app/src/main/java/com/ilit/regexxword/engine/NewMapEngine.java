package com.ilit.regexxword.engine;

import com.ilit.regexxword.bo.Cell;
import com.ilit.regexxword.bo.Const;
import com.ilit.regexxword.bo.Map;
import com.ilit.regexxword.bo.Row;

public class NewMapEngine implements IMapEngine
{
	private int _mapSize = 6;
	
	public NewMapEngine (int size)
	{
		_mapSize = size;
	}
	
	@Override
	public Map generateMap()
	{
		Map _map = new Map(_mapSize);

		// Populate all cells in the map with characters
		Cell[] _cells = _map.getCells();
		char _lastChar = CharEngine.getRandomChar();
		for (int i = 0; i < _cells.length; i++)
		{
			if (Math.random() > Const.REPEAT)
				_lastChar = CharEngine.getRandomChar();
			
			_cells[i] = new Cell(_map, _lastChar);
		}
		
		// Generate the hints in all rows
		Row[] _rows = _map.getRows();
		for (int i = 0; i < _rows.length; i++)
		{
			_rows[i] = new Row(_map, i);
			_rows[i].setHint(this.generateHint(_rows[i].getActualString()));
		}
		
		return _map;
	}
	
	private String generateHint(String input)
	{
		String[] _input = new String[] { input };

		// Note: SpecialSplitter has to be the last item in the array and has
		// to have a trigger value of 1 - it's a catch-all pattern.
		SpecialHintBase[] _specialHintSet = new SpecialHintBase[]
		{
			new Special2SetsOfChars(),
			new Special1CharRepeats3PlusTimes(),
			new Special1CharRepeatsTwice(),
			new Special2CharsInReverse(),
			new Special3CharsOnly(),
			new Special2CharRepeatsOnce(),
			new SpecialOrBlockWithTrippleChar(),
			new SpecialOrBlockForPart().setTriggerProbability(Const.PROB_OR_BLOCK_PART),
			new SpecialOrBlock().setTriggerProbability(Const.PROB_OR_BLOCK),
			new SpecialSplitter()
		};
		
		for (SpecialHintBase _sp : _specialHintSet)
			if (_sp.buildHintWithProbability(_input))
				break;

		return _input[0];
	}
}
