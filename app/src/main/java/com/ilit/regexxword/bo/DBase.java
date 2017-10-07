package com.ilit.regexxword.bo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBase extends SQLiteOpenHelper 
{
    private static final String DB_NAME = "RegexCrossword";
    private static final int DB_VERSION = 1;
    
    public DBase(Context ctx) 
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        // Create tables
    	db.execSQL(CREATE_MAP_TABLE);
        db.execSQL(CREATE_ROW_TABLE);
        db.execSQL(CREATE_CELL_TABLE);
        
        // Insert data for the MIT map
        db.execSQL(INSERT_MAP, new Object[] { 0, 7, 0 });
        
        for (int i = 0; i < MIT_MAP_CELLS.length; i++)
			db.execSQL(INSERT_CELL, new Object[] { 0, i, MIT_MAP_CELLS[i], '\0', 0 });
        
        for (int i = 0; i < MIT_MAP_HINTS.length; i++)
			db.execSQL(INSERT_ROW, new Object[] { 0, i, MIT_MAP_HINTS[i] });
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
    	// No upgrade code required at this stage.
    }
    
    
    /*============================================================================ 
	CRUD methods
	============================================================================*/ 
    
    public boolean mapExists()
    {
        SQLiteDatabase _db = this.getReadableDatabase();
        Cursor _cursor = _db.rawQuery(SELECT_MAP, new String[] { "1" });
        return (_cursor.getCount() > 0);
    }
    
    public Map getMap(int id) 
    {
        SQLiteDatabase _db = this.getWritableDatabase();
        String[] _args = new String[] { id + "" };
        
        
        // Get row size
        Cursor _cursor = _db.rawQuery(SELECT_MAP, _args);
        _cursor.moveToFirst();
        Map _map = new Map(_cursor.getInt(0));
        _map.setTimeElapsed(_cursor.getLong(1));
        
        // Get cells
        _cursor = _db.rawQuery(SELECT_CELLS, _args);
        Cell[] _cells = _map.getCells();
    	int i = 0;
        if (_cursor.moveToFirst()) 
        {
        	do {
            	_cells[i] = new Cell(_map, _cursor.getString(1).toCharArray()[0]);

            	String _userValue = _cursor.getString(2);
            	
            	if (_userValue.length() > 0)
            		_cells[i].setUserValue(_userValue.toCharArray()[0]);
            	
            	_cells[i].setFixed(_cursor.getInt(3) == 1);
            	
            	i++;

        	} while (_cursor.moveToNext());
        }
        
        
        // Get rows 
        _cursor = _db.rawQuery(SELECT_ROWS, _args);
        Row[] _rows = _map.getRows();
    	i = 0;
        if (_cursor.moveToFirst()) 
        {
        	do {
            	_rows[i] = new Row(_map, i);
            	_rows[i].setHint(_cursor.getString(1));
                i++;
            } while (_cursor.moveToNext());
        }
        
        _db.close();
        return _map;
    }
    
    public void deleteMap()
    {
	    SQLiteDatabase _db = this.getWritableDatabase();
		
	    // Delete the old map first
		_db.execSQL(DELETE_MAP);
		_db.execSQL(DELETE_CELLS);
		_db.execSQL(DELETE_ROWS);
    }
    
	public void saveMap(Map map) 
	{
	    SQLiteDatabase _db = this.getWritableDatabase();
		
	    // Delete the old map first
		_db.execSQL(DELETE_MAP);
		_db.execSQL(DELETE_CELLS);
		_db.execSQL(DELETE_ROWS);
		

		// Create new map records
		_db.execSQL(INSERT_MAP, new Object[] { 1, map.getSize(), map.getTimeElapsed() });

		Cell[] _cells = map.getCells();
		for (int c = 0; c < _cells.length; c++)
			_db.execSQL(INSERT_CELL, new Object[] 
			{ 
	    		1							,
            	c 							,
            	_cells[c].getActualValue() 	,
            	_cells[c].getUserValue() 	,
            	_cells[c].isFixed() ? 1 : 0
    		});

		Row[] _rows = map.getRows();
		for (int r = 0; r < _rows.length; r++)
			_db.execSQL(INSERT_ROW, new Object[] { 1, r,_rows[r].getHint() });
		
		_db.close();
	}
    
	
	/*============================================================================ 
	Query constants
	============================================================================*/ 
	private final static String CREATE_MAP_TABLE = 
			  "CREATE TABLE MAP (				"
			+ "			 MapID 			INTEGER,"
			+ "			 MapSize		INTEGER,"
			+ "			 TimeElapsed	INTEGER "
			+ ")								";

	private final static String CREATE_ROW_TABLE = 
			  "CREATE TABLE ROW (				"
			+ "			MapID 			INTEGER,"
			+ "			RowID 		 	INTEGER,"
			+ "			Hint 			TEXT	"
			+ ")								";	

	private final static String CREATE_CELL_TABLE = 
			  "CREATE TABLE CELL (				"
			+ "			MapID 			INTEGER,"
			+ "			CellID 			INTEGER,"
			+ "			ActualValue		TEXT,	"
			+ "			UserValue		TEXT,	"
			+ "			IsFixed			INTEGER	"
			+ ")								";

	private final static String SELECT_MAP = 
			  "SELECT 	MapSize,				"
			+ " 		TimeElapsed				"	
			+ "FROM		MAP						"
			+ "WHERE	MapID = ?				";

	private final static String SELECT_ROWS = 
			  "SELECT 	RowID,					"
			+ "			Hint					"
			+ "FROM		ROW						"
			+ "WHERE	MapID = ?				"
			+ "ORDER BY RowID					";

	private final static String SELECT_CELLS = 
			  "SELECT 	CellID,					"
			+ "			ActualValue,			"
			+ "			UserValue,				"
			+ "			IsFixed					"
			+ "FROM		CELL					"
			+ "WHERE	MapID = ?				"
			+ "ORDER BY CellID					";

	private final static String INSERT_MAP = 
			  "INSERT INTO MAP (				"
			+ "			MapID, 					"
			+ "			MapSize, 				"
			+ "			TimeElapsed				"		
			+ ")								"
			+ "VALUES (?, ?, ?)					";

	private final static String INSERT_ROW = 
			  "INSERT INTO ROW (				"
			+ "			MapID, 					"
			+ "			RowID, 					"
			+ "			Hint 		 			"
			+ ")								"
			+ "VALUES (?, ?, ?)					";

	private final static String INSERT_CELL = 
			  "INSERT INTO CELL (				"
			+ "			MapID, 					"
			+ "			CellID, 				"
			+ "			ActualValue,			"
			+ "			UserValue,				"
			+ "			IsFixed 		 		"
			+ ")								"
			+ "VALUES (?, ?, ?, ?, ?)			";

	private final static String DELETE_MAP = 
			  "DELETE FROM	MAP 				"
			+ "WHERE		MapID = 1			";

	private final static String DELETE_ROWS = 
			  "DELETE FROM	ROW 				"
			+ "WHERE 		MapID = 1			";

	private final static String DELETE_CELLS = 
			  "DELETE FROM	CELL 				"
			+ "WHERE 		MapID = 1			";
	
    private final static char[] MIT_MAP_CELLS = new char[] 
    {
		'N', 'H', 'P', 'E', 'H', 'A', 'S', 
		'D', 'I', 'O', 'M', 'O', 'M', 'T', 'H', 
		'F', 'O', 'X', 'N', 'X', 'A', 'X', 'P', 'H', 
		'M', 'M', 'O', 'M', 'M', 'M', 'M', 'R', 'H', 'H', 
		'M', 'C', 'X', 'N', 'M', 'M', 'C', 'R', 'X', 'E', 'M', 
		'C', 'M', 'C', 'C', 'C', 'C', 'M', 'M', 'M', 'M', 'M', 'M', 
		'H', 'R', 'X', 'R', 'C', 'M', 'I', 'I', 'I', 'H', 'X', 'L', 'S', 
		'O', 'R', 'E', 'O', 'R', 'E', 'O', 'R', 'E', 'O', 'R', 'E', 
		'V', 'C', 'X', 'C', 'C', 'H', 'H', 'M', 'X', 'C', 'C', 
		'R', 'R', 'R', 'R', 'H', 'H', 'H', 'R', 'R', 'U', 
		'N', 'C', 'X', 'D', 'X', 'E', 'X', 'L', 'E', 
		'R', 'R', 'D', 'D', 'M', 'M', 'M', 'M', 
		'G', 'C', 'C', 'H', 'H', 'C', 'C'
    };
    
    private final static String[] MIT_MAP_HINTS = new String[] 
	{
		".*H.*H.*"						,
		"(DI|NS|TH|OM)*"            	,
		"F.*[AO].*[AO].*"           	,
		"(O|RHH|MM)*"              		,		
		".*"                        	,	
		"C*MC(CCC|MM)*"             	,
		"[^C]*[^R]*III.*"           	,
		"(...?)\\1*"                  	,
		"([^X]|XCC)*"               	,
		"(RR|HHH)*.?"               	,
		"N.*X.X.X.*E"               	,
		"R*D*M*"                    	,
		".(C|HH)*"                  	,

		"[^X]*(DN|TE|NI)"				,
		"[CHMNOR]*I[CHMNOR]*"       	,
		".*(..)\\1P+"                	,
		"(E|RC|NM)*"                	,
		"([^MC]|MM|CC)*"            	,
		"R?(CR)*MC[AM]*"            	,
		".*"                        	,
		".*CDD.*RRP.*"              	,
		"(XHH|[^HX])*"              	,
		"([^EMC]|ME)*"              	,
		".*RXO.*"                   	,
		".*LR.*RL.*"                	,
		".*EU.*ES.*"                	,

		".*H.*V.*G.*"               	,
		"[CR]*"                     	,
		"M*XEX.*"                   	,
		".*MCC.*DD.*"               	,
		".*X.*RCHX.*"               	,
		".*(.)(.)(.)(.)\\4\\3\\2\\1.*"  ,
		"(NI|ES|IH).*"              	,
		"[^C]*MMM[^C]*"             	,
		".*(.)X\\1C\\1.*"             	,
		"[AEMOR]*HO[CEIMU]*"        	,
		"(XR|[^R])*"                	,
		"[^M]*M[^M]*"               	,
		"(S|MM|HHH)*"               
	};
}
