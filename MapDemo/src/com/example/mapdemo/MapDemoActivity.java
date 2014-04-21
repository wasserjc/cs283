package com.example.mapdemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Jared Wasserman
 * MapDemoActivity provides a UI for inputing latitude longitude
 * coordinates and displaying them on a map (either within a Google
 * Maps application or in a browser if Maps are not available).
 *
 */
public class MapDemoActivity extends Activity {

	
	// ***************************************************
	// PRIVATE DATA MEMBERS
	
	TextView inning;
	TextView homeScore;
	TextView awayScore;
	TextView outs;
	TextView balls;
	TextView strikes;
	CheckBox firstBase;
	CheckBox secondBase;
	CheckBox thirdBase;
	Button queryButton;
	
	// ***************************************************
	
	class ProcessingThread extends Thread {
		private String str_;
		ProcessingThread(String str) {
			str_ = str;
		}
		
		public void run() {
			processResponse(str_);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_demo);

		
		inning = (TextView) findViewById(R.id.inning);
		homeScore = (TextView) findViewById(R.id.home_score);
		awayScore = (TextView) findViewById(R.id.away_score);
		outs = (TextView) findViewById(R.id.outs);
		balls = (TextView) findViewById(R.id.balls);
		strikes = (TextView) findViewById(R.id.strikes);
		firstBase = (CheckBox) findViewById(R.id.first_base);
		secondBase = (CheckBox) findViewById(R.id.second_base);
		thirdBase = (CheckBox) findViewById(R.id.third_base);
		queryButton = (Button) findViewById(R.id.query_button);
		
	}

	
	public void onQueryButton(View view) {
		new Thread() {
			public void run() {
				try {
					// need to figure out the right URL that servlet expects
					String query = "?type=query";
					URL url = new URL("http://10.0.2.2:8080/BaseballScoreKeeperServer/Game" + query);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.connect();
					BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line+"\n");
					}
					br.close();
					
					// now sb.toString() should contain the JSON string
					runOnUiThread(new ProcessingThread(sb.toString()));
					
				} catch (Exception e) {
					runOnUiThread(new Thread() {
						public void run() {
							Toast.makeText(getApplicationContext(), 
									"Something went wrong.", 
									Toast.LENGTH_LONG).show();
						}
					});
				}

			}
		}.start();
	}
	
	
	private void processResponse(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			inning.setText("INNING: " + object.getString("INNING"));
			// change home / away team names each query
			homeScore.setText("HOME: " + object.getString("HOMERUNS"));
			awayScore.setText("AWAY: " + object.getString("AWAYRUNS"));
			outs.setText("OUTS: " + object.getString("OUTS"));
			balls.setText("BALLS: " + object.getString("BALLS"));
			strikes.setText("STRIKES: " + object.getString("STRIKES"));
			
			if (object.getString("FIRST") == "true")
				firstBase.setChecked(true);
			else firstBase.setChecked(false);
			
			if (object.getString("SECOND") == "true")
				secondBase.setChecked(true);
			else secondBase.setChecked(false);
			
			if (object.getString("THIRD") == "true")
				thirdBase.setChecked(true);
			else thirdBase.setChecked(false);

			Toast.makeText(getApplicationContext(), "Update Complete.", Toast.LENGTH_LONG).show();
			
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),"JSON Exception Thrown",Toast.LENGTH_LONG).show();
		}
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_demo, menu);
		return true;
	}
	
	
	
	
}
