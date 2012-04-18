package com.dovico.timesheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityAbout extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	public void onClickTimeEntry(View v) {
		Intent intent = new Intent(ActivityAbout.this, ActivityMain.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickTimesheet(View v) {
		Intent intent = new Intent(ActivityAbout.this, ActivityTimesheet.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickSetup(View v) {
		Intent intent = new Intent(ActivityAbout.this, ActivitySetup.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	public void onClickAbout(View v) {
//		Intent intent = new Intent(ActivityAbout.this, ActivityAbout.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
	}

}
