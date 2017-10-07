package com.ilit.regexxword.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ilit.regexxword.R;
import com.ilit.regexxword.bo.DBase;

public class MenuActivity extends Activity
{
	private final static String MENU_TYPE = "MENU_TYPE";
	private int _menuType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
				
		Intent _intent = this.getIntent();
		if (_intent != null)
			_menuType = _intent.getIntExtra(MENU_TYPE, 0);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (_menuType == 0)
		{
			this.setContentView(R.layout.activity_main_menu);
			
			this.findViewById(R.id.btn_new).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent _intent = new Intent(MenuActivity.this, MenuActivity.class);
					_intent.putExtra(MENU_TYPE, 1);
					MenuActivity.this.startActivity(_intent);
				}
			});
			
			DBase _db = new DBase(this.getApplicationContext());
			if (_db.mapExists())
				this.findViewById(R.id.btn_resume).setOnClickListener(new MapStarter(MapActivity.MAP_ID, 1));
			else
				this.findViewById(R.id.btn_resume).setVisibility(View.GONE);
			
			this.findViewById(R.id.btn_orig).setOnClickListener(new MapStarter(MapActivity.MAP_ID, 0));
		}
		else
		{
			this.setContentView(R.layout.activity_size_menu);
			this.findViewById(R.id.btn_small).setOnClickListener(new MapStarter(MapActivity.MAP_SIZE, 4));
			this.findViewById(R.id.btn_medium).setOnClickListener(new MapStarter(MapActivity.MAP_SIZE, 6));
			this.findViewById(R.id.btn_large).setOnClickListener(new MapStarter(MapActivity.MAP_SIZE, 8));
		}
	}
	
	
	private class MapStarter implements OnClickListener
	{
		private String _type;
		private int _value;
		
		public MapStarter(String type, int value)
		{
			_type = type;
			_value = value;
		}

		@Override
		public void onClick(View v)
		{
			Intent _intent = new Intent(MenuActivity.this, MapActivity.class);
			_intent.putExtra(_type, _value);
			MenuActivity.this.startActivity(_intent);
			
			// Finish current activity so back button goes to main menu instead
			if (_type.equals(MapActivity.MAP_SIZE))
				MenuActivity.this.finish();
		}
	}
}
