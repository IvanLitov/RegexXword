package com.ilit.regexxword.engine;

/**
 * The string is split into sections of various sizes and passed
 * to base obfuscation pattern
 */
public class SpecialSplitter extends SpecialHintBase
{
	@Override
	public boolean buildHint(String[] str)
	{
		int _start = 0;
		int _end = 0;
		int _length = str[0].length();
		StringBuilder _builder = new StringBuilder("");
		BasicDistributor _bp = new BasicDistributor();
		
		while (_start < _length)
		{
			// Never pick up more than half of the string
			_end = _start + Math.min(Util.random(_length - _start) + 1, _length / 2);
			_builder.append(_bp.buildHint(str[0].substring(_start, _end)));
			//_builder.append(str[0].substring(_start, _end).length() + "-");
			_start = _end;
		}
		
		str[0] = _builder.toString();
		return true;
	}
}
