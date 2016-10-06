package com.neowin.newsfeed;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class FeedGetter {
	public Document returningDoc;
	public Context context;

	public FeedGetter(Context ctxt) {
		context = ctxt;
	}

	public Document getDocument(String url) {
		
		/*
		FeedAsync task = new FeedAsync(context);
		task.execute(url);

		Document result;
		try {
			result = task.get();
			return result;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(httpPost,
					localContext);
			InputStream in = response.getEntity().getContent();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(in);
			Log.i("OH Feq", "Chaalech");
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("OH Feq", "nope buddy!");
		}
		Log.i("ARrrrrrrrrrrr", "Nai thayu bc!");
		return null;
	}

	public ArrayList<FeedItem> getFeedNow(Document doc, String latestGuid) {
		ArrayList<FeedItem> list = new ArrayList<FeedItem>();
		NodeList items = doc.getElementsByTagName("item");
		NodeList itemChildren;
		Node currentItem;
		Node currentChild;

		// scroll through every "item" tag
		for (int i = 0; i < items.getLength(); i++) {
			String mTitle = "We made the Neowin App";
			String mLink = "oh no link";
			String mDescr = "Oh no very very long description fits in here!";
			String mAuthor = "Us :)";
			String mPubdate = "Today";
			String mGuid = "Sample guid";
			String mImage = "Wow no image";
			currentItem = items.item(i);
			itemChildren = currentItem.getChildNodes();

			// scroll through every child as 'j'
			for (int j = 0; j < itemChildren.getLength(); j++) {
				currentChild = itemChildren.item(j);
				// Log.i("Item", currentChild.getNodeName()+"");

				if (currentChild.getNodeName().equalsIgnoreCase("guid")) {
					mGuid = currentChild.getTextContent() + "";
					if (latestGuid != null && mGuid.equals(latestGuid)) {
						Log.i("FEED GETTER", "Matching GUID found!");
						return list;

					}
					Log.i("Child Debug", mGuid);
				}

				if (currentChild.getNodeName().equalsIgnoreCase("title")) {
					mTitle = currentChild.getTextContent() + "";
					Log.i("Child Debug", mTitle);
				}
				if (currentChild.getNodeName().equalsIgnoreCase("link")) {
					mLink = currentChild.getTextContent() + "";
					Log.i("Child Debug", mLink);
				}
				if (currentChild.getNodeName().equalsIgnoreCase("description")) {
					mDescr = currentChild.getTextContent() + "";
					Spanned result = Html.fromHtml(mDescr);
					mDescr = result + "";
					mDescr = mDescr.replaceAll("\\n+", ".\n").trim();
					Log.i("Child Debug", mDescr);
				}
				if (currentChild.getNodeName().equalsIgnoreCase("author")) {
					mAuthor = currentChild.getTextContent() + "";
					Log.i("Child Debug", mAuthor);
				}
				if (currentChild.getNodeName().equalsIgnoreCase("pubDate")) {
					mPubdate = currentChild.getTextContent() + "";
					Log.i("Child Debug", mPubdate);
				}

				if (currentChild.getNodeName().equalsIgnoreCase("image")) {
					mImage = currentChild.getTextContent() + "";
					Log.i("Child Debug", mImage);
				}

			}
			list.add(new FeedItem(mTitle, mLink, mDescr, mAuthor, mPubdate,
					mGuid, mImage));

		}
		return list;
	}

	// ASYNC TASK STARTS HERE
	public class FeedAsync extends AsyncTask<String, Integer, Document> {

		ProgressDialog myDialog;
		Context ct;
		
		public FeedAsync(Context mContext)
		{
			ct = mContext;
		}

		@Override
		protected Document doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				InputStream in = response.getEntity().getContent();
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(in);
				Log.i("OH Feq", "Chaalech");
				return doc;
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("OH Feq", "nope buddy!");
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//myDialog = ProgressDialog
			//		.show(context, "", "Loading feed...", true);
			// myDialog.setMessage("Loading directions...");
		}

		@Override
		protected void onPostExecute(Document result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//if (myDialog.isShowing())
			//	myDialog.cancel();
		}

	}

}
