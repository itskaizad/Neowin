package com.neowin.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weblayout);
		Intent caller = getIntent();
		String urlToGo = caller.getStringExtra("Link");
		
		WebView wv = (WebView)findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.canGoForward();
		wv.loadUrl(urlToGo);
	}

}
