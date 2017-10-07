package com.ilit.regexxword.bo;


public class Row
{
	private Cell[] _cells;
	private String _hint;
	private int _groupIndex;
	
	/**
	 * Rows can only be initiated AFTER the cells have been initiated in the Map.
	 * This constructor determines the size of the row and populates the cells.
	 * @param map = the map containing the cells
	 * @param index = the row index
	 */
	public Row (Map map, int index)
	{
		GroupFinder _finder = null;
		int _groupSize = map.getRows().length / 3;

		if (index < _groupSize)
		{
			_groupIndex = 1;
			_finder = new GroupOneFinder(map, index);
		}
		else if (index < _groupSize * 2)
		{
			_groupIndex = 2;
			_finder = new GroupTwoFinder(map, index);
		}
		else 
		{
			_groupIndex = 3;
			_finder = new GroupThreeFinder(map, index);
		}

		_cells = _finder.getRowCells();
	}
	
	
	/*============================================================================ 
	Properties
	============================================================================*/ 
	public int getGroupIndex()
	{
		return _groupIndex;
	}
	
	public Cell[] getCells()
	{
		return _cells;
	}
	
	public int getSize()
	{
		return _cells.length;
	}
	
	public String getHint()
	{
		return _hint;
	}
	
	public void setHint(String val)
	{
		_hint = val;
	}
	
	public Cell getFirstCell()
	{
		return _cells[0];
	}
	
	public Cell getLastCell()
	{
		return _cells[_cells.length - 1];
	}
	
	public boolean isCorrect()
	{
		return this.getUserString().matches(_hint);
	}
	
	public String getActualString()
	{
		StringBuilder _builder = new StringBuilder("");
		for (Cell c : _cells)
		{
			_builder.append(c.getActualValue());
		}
		return _builder.toString();
	}
	
	
	private String getUserString()
	{
		StringBuilder _builder = new StringBuilder("");
		for (Cell c : _cells)
		{
			_builder.append(c.getUserValue());
		}
		return _builder.toString();
	}
	
	
	/*============================================================================ 
	Cell finders - these internal classes are used to map the cells from the map
	to individual rows. These can only be used in sequence - i.e. GroupThreeFinder
	can only be used after GroupTwoFinder, and GroupTwoFinder - only after 
	GroupOneFinder
	============================================================================*/ 
	
	private abstract class GroupFinder
	{
		protected int _rowToFind;
		protected int _rowSize; 
		protected int _longestRowIndex;
		protected Cell[] _mapCells;
		protected Map _map;
		
		public GroupFinder(Map map, int index)
		{
			_rowToFind 			= index;
			_rowSize 			= map.getSize();
			_longestRowIndex 	= map.getLongestRowIndex();
			_mapCells 			= map.getCells();
			_map				= map;
		}
		
		public abstract Cell[] getRowCells();
	}
	
	
	private class GroupOneFinder extends GroupFinder
	{
		public GroupOneFinder(Map map, int index)
		{
			super(map, index);
		}

		@Override
		public Cell[] getRowCells()
		{
			int _curRowIndex = 0;
			int _startCellIndex = 0;

			while (_curRowIndex < _rowToFind)
			{
				_startCellIndex += _rowSize;
				_rowSize += (_curRowIndex < _longestRowIndex ? 1 : -1);
				_curRowIndex++;
			}
			
			Cell[] _out = new Cell[_rowSize];
			
			for (int i = 0; i < _rowSize; i++)
				_out[i] = _mapCells[_startCellIndex + i];
			
			return _out;
		}
	}
	
	
	private class GroupTwoFinder extends GroupFinder
	{	
		public GroupTwoFinder(Map map, int index)
		{
			super(map, index);
			_rowToFind = index - map.getRows().length / 3;	// Get the relative row index
		}
		
		/**
		 * This class uses GroupOne rows as reference to find cells.
		 */
		public Row[] getRowSet()
		{
			int _setSize = _map.getRows().length / 3;
			Row[] _out = new Row[_setSize];
			
			for (int i = 0; i < _setSize; i++)
				_out[i] = _map.getRows()[i];
			
			return _out;
		}
		
		/**
		 * In Group 2 this method reverses the order of the cells in the array.
		 * In Group 3 it will just leave the array as is.
		 * @param input = input array to manipulate.
		 * @return output array
		 */
		public Cell[] reorder(Cell[] input)
		{
			int _length = input.length;
			Cell[] _out = new Cell[_length];

			for (int i = 0; i < _length; i++)
				_out[i] = input[_length - 1 - i];

			return _out;
		}
		

		@Override
		public Cell[] getRowCells()
		{
			Row[] _rowSet = this.getRowSet();
			
			// Row length must be the same length as the corresponding row in the reference group
			Cell[] _out = new Cell[_rowSet[_rowToFind].getSize()];
			
			// Once we cross over the longest row we'll start from a row other than 0
			int _startFrom = Math.max(0, _rowToFind - _longestRowIndex);
			
			// Start picking up the cell which corresponds to the index of the row to find
			int _indexToPick = _rowToFind;
			
			for (int i = 0; i < _out.length; i++)
			{
				_out[i] = _rowSet[_startFrom + i].getCells()[_indexToPick];
				
				// Once over the longest row, start reducing the _indexToPick value
				if ((_startFrom + i) >= _longestRowIndex) 
					_indexToPick--;
			}
			
			return reorder(_out);
		}
	}
	
	/**
	 * This class works exactly like GroupTwoFiner, but uses GroupTwo instead of GroupOne
	 * rows as reference.
	 */
	private class GroupThreeFinder extends GroupTwoFinder
	{
		public GroupThreeFinder(Map map, int index)
		{
			super(map, index);
			_rowToFind = index - map.getRows().length / 3 * 2;	// Get the relative row index
		}
		
		@Override
		public Row[] getRowSet()
		{
			int _setSize = _map.getRows().length / 3;
			Row[] _out = new Row[_setSize];
			
			for (int i = 0; i < _setSize; i++)
				_out[i] = _map.getRows()[_setSize + i];
			
			return _out;
		}
		
		@Override
		public Cell[] reorder(Cell[] input)
		{
			return input;
		}
	}
}
