package com.neowin.newsfeed;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, OnRefreshListener {

	Document docyo;
	NewsFeedAdapter nfa;
	ListView lv;
	TextView loadText;
	ArrayList<FeedItem> thisshittylist = new ArrayList<FeedItem>();
	ArrayList<FeedItem> alreadyExisting = new ArrayList<FeedItem>();
	DatabaseHelper feedHelper;
	SwipeRefreshLayout swipeLayout;
	String latestGuid;
	Context ctxt = this;
	private SharedPreferences settingsPref;
	RefreshAsync newsRefresher;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get the new toolbar and set it as the action bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_actionbar);

		lv = (ListView) findViewById(R.id.listView1);
		loadText = (TextView) findViewById(R.id.loadMsg);
		loadText.setVisibility(View.VISIBLE);
		if (!isNetworkAvailable()) {
			loadText.setText("No internet connection");
		}

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshPulldown);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout
				.setColorScheme(android.R.color.holo_blue_dark,
						android.R.color.holo_green_dark,
						android.R.color.holo_orange_dark,
						android.R.color.holo_red_dark);

		/*
		 * Intent fi = new Intent(this, FeedService.class); boolean result =
		 * getApplicationContext().bindService(fi, feedServiceConnection,
		 * Context.BIND_AUTO_CREATE);
		 */

		registerReceiver(updateFinishedReceiver, new IntentFilter(
				"FETCH_FINISHED_ACTION"));

		
		// Initially display the old feed, if there is any.
		feedHelper = new DatabaseHelper(this);
		latestGuid = feedHelper.getLatestGuid();
		if (latestGuid != null)
			alreadyExisting = feedHelper.getAllEntries();
		nfa = new NewsFeedAdapter(alreadyExisting, getApplicationContext());
		lv.setAdapter(nfa);
		lv.setOnItemClickListener(this);
		if (isNetworkAvailable())
			loadFeed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		settingsPref = getSharedPreferences("neosettings", 0);
		boolean reader = settingsPref.getBoolean("readview", false);
		FeedItem currentItem = (FeedItem) parent.getAdapter().getItem(position);
		if (reader == true) {
			if (isNetworkAvailable()) {
				loadText.setText("Loading article...");
				loadText.setVisibility(View.VISIBLE);
				Intent in = new Intent(getApplicationContext(), ReadView.class);
				String sendData = currentItem.getTitle()
						+ "GayatriDudhat"
						+ currentItem.getAuthor()
						+ "GayatriDudhat"
						+ currentItem.getImage()
						+ "GayatriDudhat"
						+ currentItem.getLink();
				in.putExtra("Link", sendData);
				startActivity(in);
			}
			else{
				loadText.setText("No internet connection");
				loadText.setVisibility(View.VISIBLE);
			}
		} else {
			if (isNetworkAvailable()) {
				Intent in = new Intent(getApplicationContext(), WebActivity.class);
				
				in.putExtra("Link", currentItem.getLink() + "");
				startActivity(in);
			}
			else{
				loadText.setText("No internet connection");
				loadText.setVisibility(View.VISIBLE);
			}
			
		}

	}

	public boolean loadFeed() {
		
		//called when activity starts for the first time and when user swipes down on list
		loadText.setText("Loading...");
		loadText.setVisibility(View.VISIBLE);

		feedHelper = new DatabaseHelper(this);
		latestGuid = feedHelper.getLatestGuid();
		Log.i("GUID from loadfeed", "guidLlatest_app = " + latestGuid);

		newsRefresher = new RefreshAsync(this);
		newsRefresher.execute("Neowin URL");
		// remaining code sent to the receiver

		return true;
	}

	@Override
	public void onRefresh() {

		// set message and load feed

		if (isNetworkAvailable()) {
			loadText.setText("Loading...");
			loadFeed();
		} else {
			loadText.setText("No internet connection");
			swipeLayout.setRefreshing(false);
		}
		loadText.setVisibility(View.VISIBLE);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onDestroy() {
		// Remove adapter reference from list
		lv.setAdapter(null);

		// Unregister the broadcast receiver defined at the end
		try {
			unregisterReceiver(updateFinishedReceiver);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	@Override
	public void onPause() {
		loadText.setText("Loading...");
		loadText.setVisibility(View.GONE);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		// TODO Auto-generated method stub
		Intent in = new Intent(this, SettingsActivity.class);
		startActivity(in);
	}

	private final BroadcastReceiver updateFinishedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "Received message yo!",Toast.LENGTH_LONG).show();

			if (newsRefresher != null) {
				try {
					if (newsRefresher.get() != null) {
						thisshittylist = newsRefresher.get();
					}
				} catch (Exception e) {
					Log.d("ASYNC RESULT", e.toString());
				}
			}

			alreadyExisting = feedHelper.getAllEntries();
			if (alreadyExisting != null && alreadyExisting.size() != 0) {
				thisshittylist.addAll(alreadyExisting);
				if(thisshittylist.size()>50)
				{
					List<FeedItem> newlist = thisshittylist.subList(0, 51);
					thisshittylist = new ArrayList<FeedItem>(newlist);
				}
			}

			feedHelper.clearAllMofo();

			// Database adds all after flushing the list
			for (int i = 0; i < thisshittylist.size(); i++) {
				feedHelper.addToDB(thisshittylist.get(i));
			}

			nfa = new NewsFeedAdapter(thisshittylist, getApplicationContext());
			lv.setAdapter(nfa);
			if (loadText.isShown())
				loadText.setVisibility(View.GONE);
			lv.setOnItemClickListener((OnItemClickListener) ctxt);

			// dismiss the swipe pull animation
			if (swipeLayout != null && swipeLayout.isRefreshing()) {
				swipeLayout.setRefreshing(false);
			}
		}
	};

}
