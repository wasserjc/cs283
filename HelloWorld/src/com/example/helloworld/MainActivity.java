package com.example.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.i("MainActivity", "onCreate() called");
    }


    @Override
	protected void onPause() {
		
    	Log.i("MainActivity", "onPause() called");
		super.onPause();
	}


	@Override
	protected void onRestart() {
		
		Log.i("MainActivity", "onRestart() called");
		super.onRestart();
	}


	@Override
	protected void onResume() {
		
		Log.i("MainActivity", "onResume() called");
		super.onResume();
	}


	@Override
	protected void onStart() {
		
		Log.i("MainActivity", "onStart() called");
		super.onStart();
	}


	@Override
	protected void onStop() {
		
		Log.i("MainActivity", "onStop() called");
		super.onStop();
	}
	
    @Override
	protected void onDestroy() {
		
    	Log.i("MainActivity", "onDestroy() called");
		super.onDestroy();
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
