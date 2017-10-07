package com.ilit.regexxword.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.WindowManager;

import com.ilit.regexxword.R;
import com.ilit.regexxword.bo.Map;
import com.ilit.regexxword.bo.Row;


/**
 * Stick stands for "measuring stick". This class is a singleton, which provides 
 * various measurements to the GUI objects.
 */
public class Stick
{
	private final float CELL_TEXT_FRACTION = 2f;
	private final int HINT_TEXT_FRACTION = 3;
	private final int HINT_TEXT_MARGIN_RATIO = 10;
	private final double[] ZOOM_RANGE = new double[] { 1, 1 / Math.sqrt(2), 0.5f , 1 / Math.sqrt(8) };
	
	private static Stick _instance;
	private int _cellHeight = 0;
	private int _cellWidth = 0;
	private int _screenHeight = 0;
	private int _screenWidth = 0;
	private int _zoomFactorIndex = 0;
	
	private Stick(Context ctx) 
	{
		// Determine size of cells on map.
		Bitmap _cellBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.cell_base);
		_cellHeight = _cellBitmap.getHeight();
		_cellWidth = _cellBitmap.getWidth();
		_cellBitmap.recycle();
		_cellBitmap = null;
		
		// Determine size of screen
		Point _pnt = new Point();
		((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE))
			.getDefaultDisplay()
			.getSize(_pnt);
		_screenHeight = _pnt.y;
		_screenWidth = _pnt.x;
	}
	
	public static Stick inst(Context ctx)
	{
		if (_instance == null)
			_instance = new Stick(ctx);
		
		return _instance;
	}
	
	public int getCellHeight()
	{
		return (int)(_cellHeight * ZOOM_RANGE[_zoomFactorIndex]);
	}
	
	public int getCellWidth()
	{
		return (int)(_cellWidth * ZOOM_RANGE[_zoomFactorIndex]);
	}
	
	public int getCellTextHeight()
	{
		return (int)(_cellHeight * ZOOM_RANGE[_zoomFactorIndex] / CELL_TEXT_FRACTION);
	}
	
	public int getHintTextHeight()
	{
		return (int)(_cellHeight * ZOOM_RANGE[_zoomFactorIndex] / HINT_TEXT_FRACTION);
	}
	
	public int getHintMargin()
	{
		return (int)(_cellHeight * ZOOM_RANGE[_zoomFactorIndex] / HINT_TEXT_MARGIN_RATIO);
	}
	
	public int getScreenHeight()
	{
		return _screenHeight;
	}
	
	public int getScreenWidth()
	{
		return _screenWidth;
	}
	
	public void setZoomFactor(boolean zoomOut)
	{
		if (zoomOut && _zoomFactorIndex < ZOOM_RANGE.length - 1)
		{
			_zoomFactorIndex++;
		}
		else if (!zoomOut && _zoomFactorIndex > 0)
		{
			_zoomFactorIndex--;
		}
	}
	
	/**
	 * How much should the map X offset be to ensure all hints in groups 2 and 3 are visible.
	 * @param map
	 * @return
	 */
	public int getMapXMargin(Map map)
	{
		String _hint = "";
		
		for (Row r : map.getRowsInGroup(2))
			if (r.getHint().length() > _hint.length())
				_hint = r.getHint();
		
		for (Row r : map.getRowsInGroup(3))
			if (r.getHint().length() > _hint.length())
				_hint = r.getHint();

		Rect _rect = new Rect();
		Paint _paint = new Paint();
		_paint.setTextSize(this.getHintTextHeight());
		_paint.getTextBounds(_hint, 0, _hint.length(), _rect);
		
		return (int)Math.ceil(Math.cos(Math.PI * 60 / 180) * (_rect.width() + this.getHintTextHeight()));
	}
	
	/**
	 * How much should the map Y offset be to ensure all hints in group 3 are visible.
	 * @param map
	 * @return
	 */
	public int getMapYMargin(Map map)
	{
		String _hint = "";
		
		for (Row r : map.getRowsInGroup(3))
			if (r.getHint().length() > _hint.length())
				_hint = r.getHint();

		Rect _rect = new Rect();
		Paint _paint = new Paint();
		_paint.setTextSize(this.getHintTextHeight());
		_paint.getTextBounds(_hint, 0, _hint.length(), _rect);

		return (int)Math.ceil(Math.sin(Math.PI * 60 / 180) * (_rect.width() + this.getHintTextHeight()));
	}
}
