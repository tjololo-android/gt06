package com.yusun.cartracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, MainService.class));
		finish();
		
	     //if (savedInstanceState == null) {
	    //	 getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
	     //}
	}	
}
