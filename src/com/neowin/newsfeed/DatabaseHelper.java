package com.neowin.newsfeed;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "FEED";
	public static final String TABLE_NAME = "FEED_ITEMS";
	private static int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE = "CREATE TABLE "
				+ TABLE_NAME
				+ " ( title TEXT , link TEXT, description TEXT, author TEXT, pubDate TEXT, guid TEXT , image TEXT )";

		// create books table
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older students table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// create fresh students table
		this.onCreate(db);

	}

	public void addToDB(FeedItem x) {
		SQLiteDatabase db = this.getWritableDatabase();
		String queryString = "insert into " + TABLE_NAME + " values('"
				+ x.getTitle().replaceAll("'", "`") + "','"
				+ x.getLink().replaceAll("'", "`") + "','"
				+ x.getDescription().replaceAll("'", "`") + "','"
				+ x.getAuthor().replaceAll("'", "`") + "','"
				+ x.getPubDate().replaceAll("'", "`") + "','"
				+ x.getGuid().replaceAll("'", "`") + "','"
				+ x.getImage().replaceAll("'", "`") + "')";
		db.execSQL(queryString);
		db.close();
		Log.i("DB_STUFF", "Added " + x.getTitle());
	}

	public String getLatestGuid() {
		SQLiteDatabase db = this.getReadableDatabase();
		String queryString = "select guid from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(queryString, null);
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			String result = cursor.getString(0);
			Log.d("FIRST GUID", result);
			return result;
		} else
			return null;
	}

	public ArrayList<FeedItem> getAllEntries() {
		SQLiteDatabase db = this.getReadableDatabase();
		String queryString = "select * from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(queryString, null);
		ArrayList<FeedItem> all = new ArrayList<FeedItem>();
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				all.add(new FeedItem(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3), cursor
								.getString(4), cursor.getString(5), cursor
								.getString(6)));
				cursor.moveToNext();
			}
			return all;
		}
		return null;
	}

	public void clearAllMofo() {
		SQLiteDatabase db = this.getWritableDatabase();
		onUpgrade(db, 0, 1); // Please ignore the zeroes , they mean shit!
	}

}
