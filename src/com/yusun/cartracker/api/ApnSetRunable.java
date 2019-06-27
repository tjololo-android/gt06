package com.yusun.cartracker.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

//else if (ACTION_IME_SET_APN.equals(intent.getAction())) {
//				String mnc = intent.getStringExtra("mnc");
//				String mcc = intent.getStringExtra("mcc");
//				String apn = intent.getStringExtra("apn");
//				String name = intent.getStringExtra("name");
//
//				Log.e(TAG, "ACTION_IME_SET_APN " + name + " " + apn + " " + mcc + " " + mnc);
//				if (name != null && apn != null && mcc != null && mnc != null) {
//					mHandler.post(new ApnSetRunable(mContext, mnc, mcc, apn, name));
//				}

class ApnSetRunable implements Runnable {

	Context mContext;
     public static final String APN = "apn";
	 public static final String NUMERIC = "numeric";
	Uri APN_URI = Uri.parse("content://telephony/carriers");
	String TAG = "ApnSetRunable";
	// public static final Uri PREFERRED_APN_URI
	// =Uri.parse("PREFERRED_APN_URI  PREFERRED_APN_URI ");
	Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	Uri uri = Uri.parse("content://telephony/carriers");
	String mMnc = "";
	String mMcc = "";
	String mApn = "";
	String mName = "";

	public int checkAPN() {
		Log.i(TAG,"mApn : "+mApn);
		int apn_id = -1;
		Cursor cr = mContext.getContentResolver().query(APN_URI, null, NUMERIC + "=?",
				new String[] { mMcc+mMnc}, null);

		while (cr.moveToNext()) {
			String result = cr.getString(cr.getColumnIndex(APN));
			Log.i(TAG,"ApnResult : "+result);
			if(result.equals(mApn)){
				
				int idIndex = cr.getColumnIndex("_id");
				apn_id = cr.getShort(idIndex);
				Log.i(TAG,"CheckApnReturn : "+apn_id);

				return apn_id;
			}
		}
		Log.i(TAG,"CheckApn : "+apn_id);
		return apn_id;
	}

	public int addAPN() {
		int id = -1;
		int apn_id = -1;
		ContentResolver resolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", mName);
		values.put("apn", mApn);
		values.put("type", "default,supl");
		values.put("numeric", mMcc + mMnc);
		values.put("mcc", mMcc);
		values.put("mnc", mMnc);
		values.put("proxy", "");
		values.put("port", "");
		values.put("mmsproxy", "");
		values.put("mmsport", "");
		values.put("user", "");
		values.put("server", "");
		values.put("password", "");
		values.put("mmsc", "");

		Cursor c = null;
		Uri newRow = resolver.insert(APN_URI, values);
		if (newRow != null) {
			c = resolver.query(newRow, null, null, null, null);
			int idIndex = c.getColumnIndex("_id");
			apn_id = c.getColumnIndex("apn_id");
			c.moveToFirst();
			id = c.getShort(idIndex);
		}
		if (c != null)
			c.close();
		return id;
	}

	public void SetAPN(int id) {
		ContentResolver resolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("apn_id", id);
		resolver.update(CURRENT_APN_URI, values, null, null);
	}

	ApnSetRunable(Context mContext, String mnc, String mcc, String apn, String name) {
		this.mContext = mContext;
		mMnc = mnc;
		mMcc = mcc;
		mApn = apn;
		mName = name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int apn_id = checkAPN();
		if (apn_id == -1) {
			apn_id = addAPN();
		}
		if (apn_id != -1) {
			SetAPN(apn_id);
		}
	}
}
