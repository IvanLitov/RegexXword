package com.ilit.regexxword.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.ilit.regexxword.R;
import com.ilit.regexxword.bo.Cell;
import com.ilit.regexxword.bo.Map;
import com.ilit.regexxword.bo.Row;
import com.ilit.regexxword.engine.NewMapEngine;

public class TestActivity extends Activity
{
	TextView _runsView;
	TextView _minErrorsView;
	TextView _maxErrorsView;
	TextView _avErrorsView;
	TextView _minHintView;
	TextView _maxHintView;
	TextView _avHintView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_test);
		_runsView		= (TextView)this.findViewById(R.id.runs);
		_minErrorsView	= (TextView)this.findViewById(R.id.min_errors);
		_maxErrorsView	= (TextView)this.findViewById(R.id.max_errors);
		_avErrorsView	= (TextView)this.findViewById(R.id.av_errors);
		_minHintView	= (TextView)this.findViewById(R.id.min_hint);
		_maxHintView	= (TextView)this.findViewById(R.id.max_hint);
		_avHintView		= (TextView)this.findViewById(R.id.av_hint);
		
		new Tester().execute();
	}
	
	private class Tester extends AsyncTask<Void, Long, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			long _minErrors 			= 1000000;
			long _maxErrors 			= 0;
			long _totalErrors 			= 0;
			long _minHintLength 		= 1000000; 
			long _maxHintLength 		= 0; 
			long _totalHintLength 		= 0;
			long _hintCount 			= 0;
			
			NewMapEngine _engine;
			Map _map;
			ArrayList<Cell> _errorCells;
			
			for (long i = 1; i <= 100; i++)
			{
				_engine = new NewMapEngine(7);
				_map = _engine.generateMap();
				_errorCells = _map.getNonUniqueCells();
				
				// Gather stats
				_minErrors = Math.min(_errorCells.size(), _minErrors);
				_maxErrors = Math.max(_errorCells.size(), _maxErrors);
				_totalErrors += _errorCells.size();
				
				for (Row r : _map.getRows())
				{
					int _hintLength = r.getHint().length();
					_minHintLength = Math.min(_hintLength, _minHintLength);
					_maxHintLength = Math.max(_hintLength, _maxHintLength);
					_totalHintLength += _hintLength;
					_hintCount++;
				}
				
				// Report progress
				this.publishProgress(
						_minErrors, _maxErrors, _totalErrors, i, 
						_minHintLength, _maxHintLength, _totalHintLength, _hintCount
					);
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Long... values)
		{
			_runsView.setText(values[3] + "");
			
			_minErrorsView.setText(values[0] + "");
			_maxErrorsView.setText(values[1] + "");
			_avErrorsView.setText((values[2] * 1.0 / values[3]) + "");
			
			_minHintView.setText(values[4] + "");
			_maxHintView.setText(values[5] + "");
			_avHintView.setText((values[6] * 1.0 / values[7]) + "");
		}
	}
}
