package com.ilit.regexxword.bo;

import java.util.ArrayList;

import com.ilit.regexxword.engine.CharEngine;


public class Map
{
	private final int _size;
	private final Cell[] _cells;
	private final Row[] _rows;
	private final char[] _alphabet;
	private long _timeElapsed = 0;
	
	public Map (int size)
	{
		_size = size;
		int _cellCount = 3 * size * (size - 1) + 1;
		int _rowCount = 6 * size - 3;
		_cells = new Cell[_cellCount];
		_rows = new Row[_rowCount];
		_alphabet = CharEngine.getAllowedChars();
	}

	/*============================================================================ 
	Public properties
	============================================================================*/ 
	public long getTimeElapsed()
	{
		return _timeElapsed;
	}
	
	public void setTimeElapsed(long value)
	{
		_timeElapsed = value;
	}
	
	public int getSize()
	{
		return _size;
	}
	
	public int getLongestRowIndex()
	{
		return _size - 1;
	}
	
	public int getLongestRowSize()
	{
		return 2 * _size - 1;
	}
	
	public Cell[] getCells()
	{
		return _cells;
	}
	
	public Row[] getRows()
	{
		return _rows;
	}
	
	public Row[] getRowsInGroup(int group)
	{
		int _start = 0;
		int _length = _rows.length / 3;
		Row[] _out = new Row[_length];
		
		switch (group)
		{
			case 1: _start = 0; 			break; 
			case 2: _start = _length; 		break;
			case 3: _start = _length * 2; 	break;
		}
			
		for (int i = 0; i < _length; i++)
			_out[i] = _rows[i + _start];
		
		return _out;
	}
	
	public int getEmptyCellsCount()
	{
		int _empty = 0;
		
		for (Cell c : _cells)
			if (c.isEmpty())
				_empty++;
		
		return _empty;
	}
	
	public float getPercentageComplete()
	{
		int _complete = 0;
		
		for (Cell c : _cells)
			if (!c.isEmpty())
				_complete++;
		
		return (float) (_complete * 1.0 / _cells.length);
	}
	
	public boolean isFullyPopulated()
	{
		for (Cell c : _cells)
			if (c.isEmpty())
				return false;
		
		return true;
	}
	
	public boolean isCorrect()
	{
		for (Row r : _rows)
			if (!r.isCorrect())
				return false;

		return true;
	}

	
	/*============================================================================ 
	Validating the Map
	============================================================================*/ 
	public ArrayList<Cell> getNonUniqueCells()
	{
		ArrayList<Cell> _out = new ArrayList<Cell>();
		
		// Set all user values to actual values
		for (Cell c : this.getCells())
			c.setUserValue(c.getActualValue());
		
		// Check that all row hints are legitimate
		for (Row r : this.getRows())
		{
			if (!r.isCorrect())
				for (Cell c : r.getCells())
					_out.add(c);
		}
		
		// Check if all cells values are unique
		for (Cell c : this.getCells())
		{
			if (this.isCellNotUnique(c))
				_out.add(c);
		}
		
		// Set all user values to blank
		for (Cell c : this.getCells())
			c.setUserValue('\0');
		
		return _out;
	}
	
	private boolean cellValueTrueForAllThreeRows(Cell c)
	{
		for (Row r : c.getRows())
			if (!r.isCorrect())
				return false;

		return true;
	}
	
	private boolean isCellNotUnique(Cell c)
	{
		char _realChar = c.getActualValue();
		boolean _out = false;
		
		for (char l : _alphabet)
		{
			if (l != _realChar)
			{
				c.setUserValue(l);
				if (this.cellValueTrueForAllThreeRows(c))
				{
					_out = true;
					break;
				}
			}
		}
		
		c.setUserValue(_realChar);
		return _out;
	}
}
