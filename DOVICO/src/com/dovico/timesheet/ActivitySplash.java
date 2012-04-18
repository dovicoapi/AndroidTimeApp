package com.dovico.timesheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class ActivitySplash extends Activity {
    protected static final String TAG = "ActivitySplash";


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
		new Thread() {
			@Override
			public void run() {

				int waited = 0;
				while (waited < 2000) {
					try {
						sleep(500);
						waited += 500;
					} catch (InterruptedException e) {
					}

				}
				
				startActivity( new Intent(ActivitySplash.this, ActivityMain.class) );
    			finish();
			}
		}.start();
    }


    
    
}