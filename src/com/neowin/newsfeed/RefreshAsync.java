package com.neowin.newsfeed;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RefreshAsync extends AsyncTask<String, Integer, ArrayList<FeedItem>> {

	ProgressDialog myDialog;
	Context ct;
	private DatabaseHelper feedHelper;
	private Document docyo;
	private ArrayList<FeedItem> thisshittylist;
	
	public RefreshAsync(Context ctxt)
	{
		ct = ctxt;
	}

	@Override
	protected ArrayList<FeedItem> doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		feedHelper = new DatabaseHelper(ct);
		String latestGuid = feedHelper.getLatestGuid();
		Log.i("GUID", "guidLlatest_app = " + latestGuid);
		Log.d("Yoyo", "In async");

		FeedGetter fg = new FeedGetter(ct);
		Log.d("Yoyo", "Here too");
		docyo = fg.getDocument("http://www.neowin.net/news/rss");
		if (docyo==null) {
			Log.d("Nullyo!", "Null che");
		}
		Log.d("Yoyo", "Here too too");
		thisshittylist = fg.getFeedNow(docyo, latestGuid);
		
		//Let main activity know that work's done!
		Intent intent = new Intent("FETCH_FINISHED_ACTION");
        ct.sendBroadcast(intent);
        
		return thisshittylist;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		//myDialog = ProgressDialog.show(ct, "", "Loading feed...", true);
		// myDialog.setMessage("Loading directions...");
	}

	@Override
	protected void onPostExecute(ArrayList<FeedItem> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//if (myDialog.isShowing())
		//	myDialog.cancel();
	}

}
