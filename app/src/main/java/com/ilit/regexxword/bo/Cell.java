package com.ilit.regexxword.bo;

public class Cell
{
	private final Map _map;
	private final char _actualValue;
	private char _userValue;
	private boolean _isFixed;
	
	public Cell (Map map, char val)
	{
		_map = map;
		_actualValue = val;
	}
	
	
	/*============================================================================ 
	Public properties
	============================================================================*/ 
	public int getIndex()
	{
		int _out = -1;
		Cell[] _cells = _map.getCells();
		for (int i = 0; i < _cells.length; i++)
		{
			if (_cells[i] == this)
			{
				_out = i;
				break;
			}
		}
		return _out;
	}
	
	public char getActualValue()
	{
		return _actualValue;
	}
	
	public char getUserValue()
	{
		return _userValue;
	}
	
	public void setUserValue(char val)
	{
		if (!_isFixed && _userValue != val)
			_userValue = val;
	}
	 
	public boolean isFixed()
	{
		return _isFixed;
	}
	
	public boolean isEmpty()
	{
		return _userValue == '\0';
	}
	
	public void setFixed(boolean val)
	{
		_isFixed = val;
	}
	
	/**
	 * Returns true if the user value matches the actual value. It is still 
	 * possible for the userValue to be correct if it's different to actual
	 * value but matches the hints for all the rows.
	 * @return
	 */
	public boolean isCorrect()
	{
		return _actualValue == _userValue;
	}
	
	
	/**
	 * Returns the three rows to which the cell belongs
	 */
	public Row[] getRows()
	{
		Row[] _out = new Row[3];
		_out[0] = this.getRow(1);
		_out[1] = this.getRow(2);
		_out[2] = this.getRow(3);
		return _out;
	}
	
	/**
	 * Returns the row from a given group to which the cell belongs
	 * @param group =  group to search in
	 * @return
	 */
	public Row getRow(int group)
	{
		Row[] _groupRows = _map.getRowsInGroup(group);
		for (Row r : _groupRows)
		{
			if (this.getIndexInRow(r) > -1)
			{
				return r;
			}
		}
		return null;
	}
	
	private int getIndexInRow(Row row)
	{
		int _out = -1;
		Cell[] _rowCells = row.getCells();
		
		for (int i = 0; i < _rowCells.length; i++)
		{
			if (_rowCells[i] == this)
			{
				_out = i;
				break;
			}
		}
		
		return _out;
	}
	
	
	/*============================================================================ 
	Event handling
	============================================================================*/ 
	public final static int SELECTED = 1;
	public final static int DESELECTED = 2;
	
	private ICellEventListener _listener;
	
	public interface ICellEventListener
	{
		public void onEvent(int event);
	}
	
	public void setEventListener(ICellEventListener listener)
	{
		_listener = listener;
	}
	
	public void raiseEvent(int event)
	{
		if (_listener != null)
			_listener.onEvent(event);
	}
}
