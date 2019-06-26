package com.yusun.cartracker.api;

import com.yusun.cartracker.R;
import com.yusun.cartracker.R.integer;
import com.yusun.cartracker.R.string;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MyPreference {
	public static final String KEY_IP = "ip";
	public static final String KEY_PORT = "port";
	public static final String KEY_GPS_INTERVAL = "gps_interval";
	public static final String KEY_LBS_INTERVAL = "lbs_interval";
	public static final String KEY_GPS_WORK_INTERVAL = "gps_work_interval";
	
	private Context mContext;
	public MyPreference(Context ctx){
		mContext = ctx;
		first();
	}	
	public void set(String key, String val){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		preferences.edit().putString(key, val);
		preferences.edit().commit();
	}
	public void set(String key, int val){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		preferences.edit().putInt(key, val);
		preferences.edit().commit();
	}
	public String getString(String key){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getString(key, "null");
	}
	public int getInt(String key){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		return preferences.getInt(key, -1);
	}
	void first(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		if("null".equals(preferences.getString("init", "null"))){
			Editor edit = preferences.edit();			
			edit.putString(KEY_IP, mContext.getString(R.string.ip));
			edit.putString(KEY_PORT, mContext.getString(R.string.port));
			edit.putInt(KEY_GPS_INTERVAL, mContext.getResources().getInteger(R.integer.gps_interval));
			edit.putInt(KEY_LBS_INTERVAL, mContext.getResources().getInteger(R.integer.lbs_interval));
			edit.putInt(KEY_GPS_WORK_INTERVAL, mContext.getResources().getInteger(R.integer.gps_work_interval));
			
			edit.putString("init", "inited");
			edit.apply();
			edit.commit();
		}
	}
}
