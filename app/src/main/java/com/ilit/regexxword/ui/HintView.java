package com.ilit.regexxword.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ilit.regexxword.bo.Row;

public class HintView extends View
{
	private final Paint _paint;
	private final Rect _rect = new Rect();
	private int _textSize;
	private Row _row;
	
	public HintView(Context context)
	{
		super(context);
		_paint = new Paint();
		this.initialize();
	}

	public HintView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		_paint = new Paint();
		this.initialize();
	}

	public HintView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		_paint = new Paint();
		this.initialize();
	}

	private void initialize()
	{
		_paint.setColor(Color.BLACK);
		_paint.setAntiAlias(true);
		_textSize = Stick.inst(this.getContext()).getHintTextHeight();
		_paint.setTextSize(_textSize);
	}
	
	/**
	 * Wires the HintView to the corresponding CellView.
	 * @param cell - CellView to attach to.
	 * @param groupIndex - determines which hint to display and how to offset
	 */
	public void setBoundCellView(CellView cell, int groupIndex)
	{
		Stick _stick = Stick.inst(this.getContext().getApplicationContext());
		_row = cell.getBoundCell().getRow(groupIndex);
		_paint.getTextBounds(_row.getHint(), 0, _row.getHint().length(), _rect);

		float _x = cell.getExpectedX();
		float _y = cell.getExpectedY();
		switch (groupIndex)
		{
			case 1:
				_x += _stick.getCellWidth() + _stick.getHintMargin();
				_y += _stick.getCellHeight() * 1 / 3;
				break;
				
			case 2:
				_x -= _rect.width();
				_y += (_stick.getCellHeight() * 2 / 3 + _stick.getHintMargin() * 2);
				break;
				
			case 3:
				_x -= _rect.width();
				_y -= (_rect.width() + _stick.getHintMargin() * 2);
				break;
		}

		RelativeLayout.LayoutParams _params = new RelativeLayout.LayoutParams (
												  RelativeLayout.LayoutParams.WRAP_CONTENT,
												  RelativeLayout.LayoutParams.WRAP_CONTENT); 
		_params.setMargins((int)_x, (int)_y, 0, 0);
		this.setLayoutParams(_params);
	}

	/**
	 * This method only handles turning and arranging the text within the draw rectangle.
	 * Aligning the hint to the corresponding CellView happens in the setBoundCellView method.
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		// This code shows hint guide lines - used for alignment
//		Rect _help = new Rect(0, 0, _rect.width() + _textSize, _rect.width() + _textSize);
//		Paint _pnt = new Paint();
//		_pnt.setColor(Color.RED);
//		_pnt.setStyle(Style.STROKE);
//		canvas.drawRect(_help, _pnt);
		
		// Note: Because the rect is made into a square using the width of the text the _rect.height
		// property does not matter - all measures are based off _rect.width()
		switch (_row.getGroupIndex())
		{
			case 1:
				canvas.drawText(_row.getHint(), 0, _textSize, _paint);
				break;
				
			case 2:
				canvas.rotate(-60, _rect.width(), 0);
				canvas.drawText(_row.getHint(), 0, _textSize, _paint);
				break;
				
			case 3:
				canvas.rotate(60, _rect.width(), _rect.width() + _textSize);
				canvas.drawText(_row.getHint(), 0, _rect.width() + _textSize, _paint);
				break;
		}
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		// Increase the actual text rectangle by the text size to allow for rotation. If not
		// set the rotated text will be clipped at the text box border.
		this.setMeasuredDimension(_rect.width() + _textSize, _rect.width() + _textSize);
	}
}
