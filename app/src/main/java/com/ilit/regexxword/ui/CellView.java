package com.ilit.regexxword.ui;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.ilit.regexxword.R;
import com.ilit.regexxword.bo.Cell;
import com.ilit.regexxword.bo.Row;
import com.ilit.regexxword.engine.CharEngine;

public class CellView 	extends 	View 
						implements 	Cell.ICellEventListener,
									View.OnKeyListener
{
	public static final int DEFAULT = 1;
	public static final int SELECTED = 2;
	public static final int FIXED = 3;
	
	private Cell _cell;
	private Paint _paint;
	private Bitmap _background;
	private Bitmap _reflection;
	private char _value;
	private Rect _textRect;
	private int _textSize;
	private int _state;
	private Stick _stick;

	private float _expX = 0;
	private float _expY = 0;
	
	// All three constructors are required to be able to handle both run-time initiation,
	// editor initiation, etc.
	public CellView(Context context)
	{
		super(context);
		_stick = Stick.inst(context);
		this.initialize();
	}

	public CellView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		_stick = Stick.inst(context);
		this.initialize();
	}
	
	public CellView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		_stick = Stick.inst(context);
		this.initialize();
	}

	private void initialize()
	{
		this.setState(DEFAULT);
		
		_textSize = _stick.getCellTextHeight();
		_paint = new Paint();
		_paint.setColor(Color.BLACK);
		_paint.setAntiAlias(true);
		_paint.setTextSize(_textSize);
		_textRect = new Rect();
		
		this.setFocusableInTouchMode(true);
		this.setOnKeyListener(this);
		
		_reflection = BitmapFactory.decodeResource(this.getResources(), R.drawable.cell_reflection);
		_reflection = Bitmap.createScaledBitmap(_reflection, _stick.getCellWidth(), _stick.getCellHeight(), true);
	}

	
	/**
	 * Similar concept to setLayoutParams but stores teh x and y values separately,
	 * so they can be reused for hints before the map is physically drawn.
	 * @param x
	 * @param y
	 */
	public void setMargins(int x, int y)
	{
		_expX = x;
		_expY = y;
		
		RelativeLayout.LayoutParams _params = new RelativeLayout.LayoutParams (
				  RelativeLayout.LayoutParams.WRAP_CONTENT,
				  RelativeLayout.LayoutParams.WRAP_CONTENT); 

		_params.setMargins(x, y , 0, 0);
		this.setLayoutParams(_params);

	}
	
	public float getExpectedX()
	{
		return _expX;
	}
	
	public float getExpectedY()
	{
		return _expY;
	}
	
	public Cell getBoundCell()
	{
		return _cell;
	}
	
	public void setBoundCell(Cell cell)
	{
		_cell = cell;
		_cell.setEventListener(this);
		this.setValue(cell.getUserValue());
		
		if (_cell.isFixed())
			this.setState(FIXED);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Draw background
		canvas.drawBitmap(_background, 0, 0, _paint);
		
		// Draw text
		int _xOffset = (_background.getWidth() - _textRect.width()) / 2 - _textRect.left;
		int _yOffset = (_background.getHeight() - _textRect.height()) / 2 + _textRect.height();
		canvas.drawText(_value + "", _xOffset, _yOffset, _paint);
		
		// Draw reflection
		canvas.drawBitmap(_reflection, 0, 0, _paint);
	}
	
	
	public void setValue(char value)
	{
		
		if (_cell == null) return;
		
		if (value == '\0' && _cell.isFixed()) return;
		
		_cell.setUserValue(value);

		if (_value != value)
		{
			_value = value;
			_paint.getTextBounds(_value + "", 0, 1, _textRect);
			if (_cellEventListener != null)
				_cellEventListener.onCellValueChanged();

			this.invalidate();
		}
	}
	
	
	public void setState(int state)
	{
		if (_state == state)
			return;
		
		int _bmp = 0;
		switch (state)
		{
			case DEFAULT:	_bmp = R.drawable.cell_base;		break;
			case SELECTED:	_bmp = R.drawable.cell_selected;	break;
			case FIXED:		_bmp = R.drawable.cell_fixed;		break;
		}
		
		// Scale the image
		_background = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(this.getResources(), _bmp), 
				_stick.getCellWidth(), _stick.getCellHeight(), true);

		_state = state;
		this.invalidate();
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		this.setMeasuredDimension(_background.getWidth(), _background.getHeight());
	}

	@Override
	public void onEvent(int event)
	{
		switch (event)
		{
			case Cell.SELECTED:		this.setState(SELECTED);							break;
			case Cell.DESELECTED:	this.setState(_cell.isFixed() ? FIXED : DEFAULT);	break;
		}
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
	{
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		
		int _event = gainFocus ? Cell.SELECTED : Cell.DESELECTED;
		
		// Raise cell in focus event
		if (gainFocus && _cellEventListener != null)
			_cellEventListener.onCellGetFocus(this);;
		
		// Apply highlights
		for (Row r : _cell.getRows())
			for (Cell c : r.getCells())
				c.raiseEvent(_event);
	}

	/*============================================================================ 
	Interface for handling cell view events
	============================================================================*/ 
	public interface CellEventListener
	{
		void onCellValueChanged();
		void onCellGetFocus(CellView view);
	}
	
	private CellEventListener _cellEventListener;
	
	public void setCellValueChangedListener(CellEventListener listener)
	{
		_cellEventListener = listener;
	}
	
	
	/*============================================================================ 
	This section handles the soft keyboard
	============================================================================*/ 
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) 
        {
            // show the keyboard so we can enter text
            InputMethodManager _imm = (InputMethodManager)getContext()
            							.getSystemService(Context.INPUT_METHOD_SERVICE);
            _imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        }
        return true;
    }
    
    public void hideSoftKeyboard()
    {
		try
		{
	        InputMethodManager _imm = (InputMethodManager)this.getContext()
	        							.getSystemService(Context.INPUT_METHOD_SERVICE);
	        _imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		} catch (Exception e) {}
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) 
    {
        BaseInputConnection _fic = new BaseInputConnection(this, false);
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_NULL;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_NEXT;
        return _fic;
    }	

    @SuppressLint("DefaultLocale")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		if (keyCode == 4)
		{
			// User clicked the back button - close the activity
			if (this.getContext() != null && this.getContext().getClass() == MapActivity.class)
				((MapActivity)this.getContext()).finish();
		}
		else if (keyCode == 67)
		{
			// User clicked backspace on keyboard - clear the cell
			this.setValue('\0');
		}
		else 
		{
			// Validate if the input is a legit character and is in the list of allowed characters.
			String _inputString = (char)event.getUnicodeChar() + "";
			if (_inputString.length() > 0)
			{
				char _inputChar = _inputString.toUpperCase(Locale.getDefault()).toCharArray()[0];
				for (char c : CharEngine.getAllowedChars())
				{
					if (c == _inputChar)
					{
						this.setValue(_inputChar);
						break;
					}
				}
			}
			else
			{
				// Do nothing.
			}
		}
		return true;
	}
}
