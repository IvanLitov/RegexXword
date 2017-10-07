package com.ilit.regexxword.bo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.AsyncTask;

public class GameTimer extends AsyncTask<Void, Long, Void>
{
	private long _timeSpan = 0;
	private boolean _stop = false;
	
	// If this is not applied, time formatter returns 1am during summer time
	public static final long BASE = new GregorianCalendar(2000, 1, 1).getTimeInMillis();

	public GameTimer (long timeSpan)
	{
		_timeSpan = timeSpan;
	}
	
	public synchronized long getTimeSpan()
	{
		return _timeSpan;
	}
	
	private synchronized void setTimeSpan(long value)
	{
		_timeSpan = value;
	}
	
	// Does not have to be synchronised because boolean is already atomic
	public void stop()
	{
		_stop = true;
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		long _millis = new Date().getTime();
		while (true)
		{
			if (_stop) return null;
				
			try
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {}
			
			long _now = new Date().getTime();
			long _newTimeSpan = this.getTimeSpan() + _now - _millis;
			_millis = _now;
			this.publishProgress(_newTimeSpan);
			this.setTimeSpan(_newTimeSpan);
		}
	}
	
	public static String getTimeString(long millis)
	{
		SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		Date _date = new Date();
		_date.setTime(BASE + millis);
		return _sdf.format(_date);
	}
}
