package com.neowin.newsfeed;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends ActionBarActivity 
{
	SharedPreferences settingsPref; 
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingslayout);
		
		// get the new toolbar and set it as the action bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_actionbar);
		
		TextView dt = (TextView)findViewById(R.id.design);
		TextView ka = (TextView)findViewById(R.id.kaizada);
		TextView bg = (TextView)findViewById(R.id.bharatg);
		TextView na = (TextView)findViewById(R.id.narayana);
		Typeface tf = Typeface.createFromAsset(getAssets(), "Barkentina.otf");
		dt.setTypeface(tf);
		ka.setTypeface(tf);
		bg.setTypeface(tf);
		na.setTypeface(tf);

		settingsPref = getSharedPreferences("neosettings", 0);
		final SharedPreferences.Editor editor = settingsPref.edit();
		
		Switch sw1 = (Switch)findViewById(R.id.switch1);

		if(settingsPref.getBoolean("readview", false))
			sw1.setChecked(true);
		
		sw1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!isChecked)
				{
					editor.putBoolean("readview", false);
					editor.commit();
				}
				else
				{
					editor.putBoolean("readview", true);
					editor.commit();
				}
			}
		});
		
		
	}
}
