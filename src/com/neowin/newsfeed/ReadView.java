package com.neowin.newsfeed;

import im.delight.imagescraper.ImageScraper;
import im.delight.imagescraper.ImageScraperCallback;
import im.delight.imagescraper.ImageScraperResult;

import java.io.FileNotFoundException;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neowin.textextractor.WebsiteTextExtractor;
import com.neowin.utils.ImageLoader;

public class ReadView extends ActionBarActivity implements ImageScraperCallback, OnInitListener {

	private ImageLoader imageLoader;
	ImageView img;
	String info[];
	TextView title, auth, article;
	private TextToSpeech myTTS;
	private int MY_DATA_CHECK_CODE = 0;
	Context ctxt = this;
	TextView stopButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readinglayout);
		Intent caller = getIntent();
		String dataRecd = caller.getStringExtra("Link");

		// get the new toolbar and set it as the action bar
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		toolbar.setLogo(R.drawable.ic_actionbar);
		imageLoader = new ImageLoader(this);

		stopButton = (TextView) findViewById(R.id.voiceButton);
		//stopButton.setVisibility(View.GONE);
		if (myTTS!=null && myTTS.isSpeaking()) {
			stopButton.setVisibility(View.VISIBLE);
		}
		stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (myTTS!=null && myTTS.isSpeaking()) {
					myTTS.stop();
					stopButton.setVisibility(View.GONE);
				}
			}
		});

		Typeface tf = Typeface.createFromAsset(getAssets(), "RobotoLight.ttf");
		title = (TextView) findViewById(R.id.titletext);
		title.setTypeface(tf);
		auth = (TextView) findViewById(R.id.authortext);
		article = (TextView) findViewById(R.id.articletext);
		article.setTypeface(tf);
		img = (ImageView) findViewById(R.id.imageView1);

		info = dataRecd.split("GayatriDudhat");
		title.setText(info[0] + "");

		try {
			WebsiteTextExtractor we = new WebsiteTextExtractor();
			String s = we.cleanFromURL(info[3] + "");
			String[] text = s.split("\n");
			auth.setText(text[0] + "");
			String passage = "";
			for (int i = 1; i < text.length; i++) {
				passage = passage + text[i] + "\n";
			}
			article.setText(passage + "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new ImageScraper(this, info[3] + "", 3).start();

		// check for TTS data
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

	}

	// image scraper method
	@Override
	public void onFinished(ImageScraperResult output) {
		// TODO Auto-generated method stub
		final String[] imageFileURLs = output.getImageURLs();
		// Toast.makeText(this, output.getURL()+"", Toast.LENGTH_LONG).show();
		if (imageFileURLs != null && imageFileURLs.length > 0) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					imageLoader.DisplayImage(imageFileURLs[0], img);
					String extra = "";
					for (int i = 0; i < imageFileURLs.length; i++) {
						extra += imageFileURLs[i] + "\n";
					}
					// article.setText(article.getText()+extra+"");
				}
			});
		}

		img.setAdjustViewBounds(true);
	}

	// image scraper method
	@Override
	public void onStarted() {
		// TODO Auto-generated method stub

	}

	private void speakWords(String speech) {
		myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);
	}

	//receives TTS check data
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
            myTTS = new TextToSpeech(this, this);
            }
            else {
                    //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

	//Initialize TT stuff
	public void onInit(int initStatus) {
	     
        //check for successful instantiation
    if (initStatus == TextToSpeech.SUCCESS) {
        if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
            myTTS.setLanguage(Locale.US);
    }
    else if (initStatus == TextToSpeech.ERROR) {
        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
    }
    myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
		
		@Override
		public void onStart(String utteranceId) {
			// TODO Auto-generated method stub
			stopButton.setVisibility(View.VISIBLE);
		}
		
		@Override
		@Deprecated
		public void onError(String utteranceId) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onDone(String utteranceId) {
			// TODO Auto-generated method stub
		}
	});
}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.reader_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.play_audio_button:
			playAudio();
			return true;
		case R.id.open_webview:
			goWeb();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void goWeb() {
		// TODO Auto-generated method stub
		Intent in = new Intent(getApplicationContext(), WebActivity.class);
		in.putExtra("Link", info[3] + "");
		startActivity(in);
	}

	private void playAudio() {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Coming real soon!", Toast.LENGTH_SHORT).show();
		String[] fragments = (article.getText()+"").split("\n");
		for (int i = 0; i < fragments.length; i++) {
			speakWords(fragments[i]);
		}
		stopButton.setVisibility(View.VISIBLE);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (myTTS!=null && myTTS.isSpeaking()) {
			myTTS.stop();
		}
		super.onDestroy();
	}

	
}
