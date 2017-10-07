package com.ilit.regexxword.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilit.regexxword.R;
import com.ilit.regexxword.bo.Cell;
import com.ilit.regexxword.bo.Const;
import com.ilit.regexxword.bo.DBase;
import com.ilit.regexxword.bo.GameTimer;
import com.ilit.regexxword.bo.Map;
import com.ilit.regexxword.bo.Row;
import com.ilit.regexxword.engine.ExistingMapEngine;
import com.ilit.regexxword.engine.IMapEngine;
import com.ilit.regexxword.engine.NewMapEngine;
import com.ilit.regexxword.ui.CellView.CellEventListener;

public class MapActivity extends Activity implements CellEventListener, OnClickListener
{
	public final static String MAP_SIZE = "MAP_SIZE";
	public final static String MAP_ID = "MAP_ID";
	
	// Used to restore activity.
	private String _mode;
	private int _modeValue;
	
	private ScaleGestureDetector _zoomDetector;
	private boolean _processPinchEvent = true;
	
	private RelativeLayout _layout;
	private LinearLayout _progressBarContainer;
	private TextView _pcCompleteView;
	private TextView _timerView;
	private GameDialogView _dialog;
	private CellView _activeCellView;
	
	private Map _map;
	private Stick _stick;
	private GameTimer _timer;
	
	public Map getMap()
	{
		return _map;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		_layout = (RelativeLayout)this.findViewById(R.id.map_canvas);
		_progressBarContainer = (LinearLayout)this.findViewById(R.id.progress_bar_container);
		_pcCompleteView = (TextView)this.findViewById(R.id.pc_complete);
		_timerView = (TextView)this.findViewById(R.id.timer);
		_dialog = (GameDialogView)this.findViewById(R.id.dialog);
		
		_stick = Stick.inst(this);
		_zoomDetector = new ScaleGestureDetector(this, new ZoomGestureListener());
		
		// Add padding to the bottom of the screen to allow the user to view
		// hints from group 2 when keyboard is visible.
		_layout.setPadding(0, 0, 0, Stick.inst(this).getScreenHeight());
		
		// Assign mode and value, which will be used by onResume. Cannot start the map from 
		// here because the timer needs to be paused when activity is out of focus.
		if (savedInstanceState != null)
		{
			_mode = MAP_ID;
			_modeValue = savedInstanceState.getInt(MAP_ID);
		}
		else
		{
			Intent _intent = this.getIntent();
			if (_intent != null)
			{
				int _mapSize = _intent.getIntExtra(MAP_SIZE, 0);
				if (_mapSize > 0)
				{
					_mode = MAP_SIZE;
					_modeValue = _mapSize;
				}
				else
				{
					_mode = MAP_ID;
					_modeValue = _intent.getIntExtra(MAP_ID, 1);
				}
			}
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (_mode == MAP_ID)
			this.generateExistingMap(_modeValue);
		else if (_mode == MAP_SIZE)
			this.generateNewMap(_modeValue);
		
		// Override the mode and value - if activity resumes before
		// it's destroyed, you don't want it to generate a new map.
		_mode = MAP_ID;
		_modeValue = 1;
	}
	
	@Override
	protected void onPause()
	{
		this.hideSoftKeyboard();
		
		// Map can be null if it's been unassigned on destroy
		if (_map != null)
		{
			if (_timer != null)
			{
				_map.setTimeElapsed(_timer.getTimeSpan());
				_timer.stop();
			}
			
			DBase _db = new DBase(this.getApplicationContext());
			_db.saveMap(_map);
		}

		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(MAP_ID, 1);
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Even though this event handler does not do anything it's needed (for some reason)
	 * or the cells don't get focus.
	 */
	@Override
	public void onClick(View arg0) {}

	/**
	 * Updates progress & validates the map after each cell value changed.
	 */
	@Override
	public void onCellValueChanged()
	{
		this.updatePercentageComplete();
		if (_map.isFullyPopulated())
		{
			this.hideSoftKeyboard();
			_dialog.show(GameDialogView.FINAL);
		}
	}
	
	@Override
	public void onCellGetFocus(CellView view)
	{
		_activeCellView = view;
	}

	/**
	 * Picks up the initial gesture and pushes it on to the gesture detector
	 * Used to zoom the map in/out
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_UP)
		{
			// Gesture ended, can start picking a new one
			_processPinchEvent = true;
		}
		else if (_processPinchEvent)
		{
			_zoomDetector.onTouchEvent(ev);
		}
			
		return super.dispatchTouchEvent(ev);
	}

	/*============================================================================ 
	Methods
	============================================================================*/ 
	
	public void generateNewMap()
	{
		this.generateNewMap(_map.getSize());
	}
	
	public void generateNewMap(int size)
	{
		_layout.removeAllViews();
		_progressBarContainer.setVisibility(View.VISIBLE);
		_timerView.setText("");
		_pcCompleteView.setText("");
		new MapAsyncGenerator(size).execute();
	}
	
	public void generateExistingMap(int id)
	{
		_map = new ExistingMapEngine(this.getApplicationContext(), id).generateMap();
		this.drawMap();
		this.startTimer();
		_progressBarContainer.setVisibility(View.GONE);
	}
	
	public void destroyMapAndActivity()
	{
		DBase _db = new DBase(this.getApplicationContext());
		_db.deleteMap();
		_timer.stop();
		_map = null;	// un-assign map variable so it doesn't get saved onPause.
		this.finish();
	}
	
	public void drawMap()
	{
		// Clear any existing cells and hints
		_layout.removeAllViews();
		
		Row[] _rows = _map.getRowsInGroup(1);
		
		//Determine the size of everything
		final int _xBase = _stick.getMapXMargin(_map);
		final int _yBase = (int)(_stick.getMapYMargin(_map) * (1f + Const.MAP_BUFFER)); 
		final int _dx = _stick.getCellWidth();
		final int _dy = _stick.getCellHeight() * 3 / 4;
		int _x = 0;
		int _y = 0;
		
		// Draw the map cells
		for (int r = 0; r < _rows.length; r++)
		{
			_x = _xBase + (_map.getLongestRowSize() - _rows[r].getSize()) * _dx / 2;
			_y = _yBase + _dy * r;
			
			Cell[] _cells = _rows[r].getCells();
			for (int c = 0; c < _cells.length; c++)
			{
				// Draw the cell
				CellView _cellView = new CellView(this);
				_cellView.setBoundCell(_cells[c]);
				_cellView.setMargins(_x, _y);
				_cellView.setOnClickListener(this);
				_cellView.setCellValueChangedListener(this);
				_layout.addView(_cellView);
				
				// Draw hints
				if (r == 0)
				{
					this.drawHint(_cellView, 3);		// This is a reference for a Group 3 row
				}
				else if (r == _rows.length - 1)
				{
					this.drawHint(_cellView, 2);		// This is a reference for a Group 2 row
				}
				
				if (c == 0)
				{
					// This is a reference for a Group 2 or 3 row
					int _longestRowIndex = _map.getLongestRowIndex();
					if (r < _longestRowIndex)
					{
						this.drawHint(_cellView, 3);	// This is a reference for a Group 3 row
					}
					else if (r > _longestRowIndex)
					{ 
						this.drawHint(_cellView, 2);	// This is a reference for a Group 2 row
					}
					else 
					{
						this.drawHint(_cellView, 3);	// This is a reference for a Group 3 row
						this.drawHint(_cellView, 2);	// This is a reference for a Group 2 row
					}
				}
				else if (c == _cells.length - 1)
				{
					this.drawHint(_cellView, 1);		// This is a reference for a Group 1 row
				}

				_x += _dx;
			}
		}
		
		this.updatePercentageComplete();
	}
	
	private void hideSoftKeyboard()
	{
		if (_activeCellView != null)
			_activeCellView.hideSoftKeyboard();
	}

	private void startTimer()
	{
		_timer = new GameTimer(_map.getTimeElapsed())
		{
			@Override
			protected void onProgressUpdate(Long... values)
			{
				super.onProgressUpdate(values);
				
				// If map is null, it's probably been destroyed once completed. Ignore call.
				if (_map != null)
					_map.setTimeElapsed(values[0]);
				
				// If timer is null, it's probably been stopped and this is the last
				// cycle reporting progress - ignore it.
				if (_timer != null)
					_timerView.setText(GameTimer.getTimeString(values[0]));
			}
		};
		_timer.execute();
	}
	
	
	private void drawHint(CellView cellView, int group)
	{
		HintView _hintView = new HintView(this);
		_hintView.setBoundCellView(cellView, group);
		_layout.addView(_hintView);
	}
	
	private void updatePercentageComplete()
	{
		_pcCompleteView.setText(_map.getEmptyCellsCount() + " cells left");
	}
	
	/**
	 * Gesture listener - used to zoom the map in/out
	 */
	private class ZoomGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{ 
			if (detector.getScaleFactor() > 1.1)
			{
				// Zoom in event detected
				_processPinchEvent = false;
				_stick.setZoomFactor(false);
				MapActivity.this.drawMap();
			}
			else if (detector.getScaleFactor() < 0.9)
			{
				// Zoom out event detected
				_processPinchEvent = false;
				_stick.setZoomFactor(true);
				MapActivity.this.drawMap();
			}
			
			return true;
		}
	}
	
	/**
	 * Used to validate if the map contains unique cells. Runs as an async task.
	 */
	private class MapAsyncGenerator extends AsyncTask<Void, Integer, ArrayList<Cell>>
	{
		private final int _mapSize;
		
		public MapAsyncGenerator(int size)
		{
			_mapSize = size;
			
			// This is a safety move, if not stopped the timer will prevent the generator
			// from starting
			if (_timer != null)
			{
				_timer.stop();
				_timer = null;
			}
		}
		
		@Override
		protected ArrayList<Cell> doInBackground(Void... params)
		{
			ArrayList<Cell> _errorCells = null;
			IMapEngine _engine = new NewMapEngine(_mapSize);
			_map = _engine.generateMap();
			_errorCells = _map.getNonUniqueCells();
			return _errorCells;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Cell> result)
		{
			_progressBarContainer.setVisibility(View.GONE);

			// First version is for production
			for (Cell c : result)
			{
				c.setUserValue(c.getActualValue());
				c.setFixed(true);
			}

			MapActivity.this.drawMap();
			MapActivity.this.startTimer();

			// Check if the map came out with too many non-unique values and offer to re-draw.
			if (result.size() * 1.0 / _map.getCells().length > Const.MAX_BAD_CELLS)
				_dialog.show(GameDialogView.BAD_MAP);
		}
	}
}
