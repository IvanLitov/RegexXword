package com.ilit.regexxword.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilit.regexxword.R;
import com.ilit.regexxword.R.id;
import com.ilit.regexxword.bo.Cell;
import com.ilit.regexxword.bo.GameTimer;
import com.ilit.regexxword.bo.Map;

public class GameDialogView extends LinearLayout
{
	public static final int BAD_MAP = 1;
	public static final int FINAL = 2;
	
	private MapActivity _parent;
	private ImageView _image;
	private TextView _mainText;
	private TextView _subText;
	private Button _positiveButton;
	private Button _negativeButton;
	
	// All three constructors are required to be able to handle both run-time initiation,
	// editor initiation, etc.
	public GameDialogView(Context context)
	{
		super(context);
		this.initialize(context);
	}

	public GameDialogView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.initialize(context);
	}
	
	public GameDialogView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		this.initialize(context);
	}

	private void initialize(Context context)
	{
		if (context instanceof MapActivity)
			_parent = (MapActivity)context;

		View _container = View.inflate(context, R.layout.game_dialog_layout, this);
		_image = (ImageView) _container.findViewById(R.id.image);
		_mainText = (TextView) _container.findViewById(R.id.main_text);
		_subText = (TextView) _container.findViewById(R.id.sub_text);
		_positiveButton = (Button) _container.findViewById(id.positive_button);
		_negativeButton = (Button) _container.findViewById(R.id.negative_button);
	}
	
	public void show(int mode)
	{
		this.setVisibility(View.VISIBLE);
		
		switch (mode)
		{
			case BAD_MAP:
				_image.setVisibility(View.GONE);
				_mainText.setText(R.string.bad_map_main);
				_subText.setText(R.string.bad_map_sub);
				_negativeButton.setVisibility(View.VISIBLE);
				_negativeButton.setText(R.string.no);
				_negativeButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						GameDialogView.this.hide();
					}
				});
				_positiveButton.setText(R.string.yes);
				_positiveButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						GameDialogView.this.hide();
						_parent.generateNewMap();
					}
				});
				
				break;
				
			case FINAL:
				_image.setVisibility(View.VISIBLE);
			    if (_parent.getMap().isCorrect())
			    {
			    	_image.setImageResource(R.drawable.img_success);
			    	_mainText.setText(R.string.final_success_main);
			    	_subText.setText(_parent.getString(R.string.final_success_sub) + 
			    			" " + GameTimer.getTimeString(_parent.getMap().getTimeElapsed()));
			    	_negativeButton.setVisibility(View.GONE);
			    	_positiveButton.setText(R.string.ok);
			    	_positiveButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							_parent.destroyMapAndActivity();
						}
					});
			    }
			    else
			    {
			    	_image.setImageResource(R.drawable.img_failure);
			    	_mainText.setText(R.string.final_fail_main);
			    	_subText.setText(R.string.final_fail_sub);
			    	_negativeButton.setVisibility(View.VISIBLE);
			    	_negativeButton.setText(R.string.no);
			    	_negativeButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							GameDialogView.this.hide();
						}
					});
			    	_positiveButton.setText(R.string.yes);
			    	_positiveButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Map _map = _parent.getMap();
							for (Cell c : _map.getCells())
								if (!c.isCorrect())
									c.setUserValue('\0');
							
							// Need to re-draw map because the map itself has no way of notifying the 
							// GUI that values have been updated.
							GameDialogView.this.hide();
							_parent.drawMap();
							
						}
					});
			    }
				break;
		}
	}
	
	public void hide()
	{
		this.setVisibility(View.GONE);
	}
}
