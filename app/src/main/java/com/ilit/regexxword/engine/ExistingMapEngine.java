package com.ilit.regexxword.engine;

import android.content.Context;

import com.ilit.regexxword.bo.DBase;
import com.ilit.regexxword.bo.Map;

public class ExistingMapEngine implements IMapEngine
{
	private int _mapID = 0;
	private Context _context;
	
	public ExistingMapEngine(Context ctx, int mapID)
	{
		_context = ctx;
		_mapID = mapID;
	}
	
	@Override
	public Map generateMap()
	{
		DBase _db = new DBase(_context);
		return _db.getMap(_mapID);
	}
}
