package com.ilit.regexxword.engine;

import java.util.ArrayList;

/**
 * This class distributes the incoming strings to base
 * pattern classes based on input characteristics and probability. 
 */
public class BasicDistributor extends BasicHintBase
{
	private final ArrayList<BasicHintBase> _bps;
	private final boolean _doNotRepeat;
	private BasicHintBase _lastHint = null;
	
	public BasicDistributor()
	{
		this(true);
	}
	
	/**
	 * Constructor with doNotRepeate parameter
	 * @param doNotRepeat = by default set to true, so that the same hint type
	 * cannot be chosen twice in a row.
	 */
	public BasicDistributor(boolean doNotRepeat)
	{
		// Basic patterns can be added to the collection more than once to increase
		// the probability of trigger and reduce the probability of characters returned
		// "as is" which is the default behaviour.
		_bps = new ArrayList<BasicHintBase>();
		_bps.add(new BasicDotStar());
		_bps.add(new BasicLeaveAsIs());
		_bps.add(new BasicRangeBlock());
		_bps.add(new BasicNotRangeBlock());
		_doNotRepeat = doNotRepeat;
	}
	
	@Override
	public String buildHint(String str)
	{
		BasicHintBase _bh = null;
		
		// Choose which pattern to apply
		if (_doNotRepeat && _lastHint != null)
		{
			do {
				_bh = _bps.get(Util.random(_bps.size()));
			} while (_bh.getMaxStringSize() < str.length() || _bh == _lastHint);
			_lastHint = _bh;
		}
		else
		{
			do {
				_lastHint = _bps.get(Util.random(_bps.size()));
			} while (_lastHint.getMaxStringSize() < str.length());
		}
		
		return _lastHint.buildHint(str);
	}
}
