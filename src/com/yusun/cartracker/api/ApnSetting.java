package com.yusun.cartracker.api;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ApnSetting {
	private final static String TAG = "ImeApnSetting";

	private Context mContext;
	private TelephonyManager mTelephonyMgr;

	private List<APN> apnList = new ArrayList<APN>();

	long rxLast = 0;
	long txLast = 0;

	public ApnSetting(Context context) {
		mContext = context;	

		mTelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);// 鍙栧緱鐩稿叧绯荤粺鏈嶅姟

		rxLast = TrafficStats.getMobileRxBytes();
		txLast = TrafficStats.getMobileTxBytes();

		bytesCheckingStart();
	}

	private static int count = 0;
	private void bytesCheckingStart() {
		new Thread(new Runnable() {
			public void run() {
				initApnList();
				while (readSIMCard() != TelephonyManager.SIM_STATE_READY) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (!isSimRight()) {
					return;
				}

				while (true) {

					long rx = TrafficStats.getMobileRxBytes();
					long tx = TrafficStats.getMobileTxBytes();

					Log.v(TAG, "Rx:" + (rx - rxLast));
					Log.v(TAG, "Tx:" + (tx - txLast));

					if ((rx - rxLast) != 0) {
						Log.i(TAG, "connect is OK " + (rx - rxLast));

						return;
					}

					rxLast = rx;
					txLast = tx;

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (count++ % 60 == 1) {
						changeCurrentApn();
					}
				}
			}
		}).start();
	}

	public int readSIMCard() {

		int state = mTelephonyMgr.getSimState();
		switch (mTelephonyMgr.getSimState()) {

		case TelephonyManager.SIM_STATE_UNKNOWN:
			Log.i(TAG, "鏈煡鐘舵��");
			break;
		case TelephonyManager.SIM_STATE_ABSENT:
			Log.i(TAG, "鏃犲崱");
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			Log.i(TAG, "闇�瑕丳IN瑙ｉ攣");
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			Log.i(TAG, "闇�瑕丳UK瑙ｉ攣");
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			Log.i(TAG, "闇�瑕丯etworkPIN瑙ｉ攣");
			break;
		case TelephonyManager.SIM_STATE_READY:
			Log.i(TAG, "鑹ソ");
			break;
		}
		return state;
	}
	// *****************APN Setting************************

	private Uri CARRIERS_APN_URI = Uri.parse("content://telephony/carriers");
	private Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	private String NUMERIC = "numeric";
	private String MCC = "mcc";
	private String MNC = "mnc";
	private String APN = "apn";
	private String ID = "_id";
	private String NAME = "name";

	private APN getCurrentApnInfo() {

		APN mApn = null;

		int mId = getCurrentApnId();
		if (mId == -1) {
			return null;
		}

		Cursor cr = mContext.getContentResolver().query(CARRIERS_APN_URI, null, ID + "=?",
				new String[] { String.valueOf(mId) }, null);

		if (cr.moveToNext()) {
			mApn = new APN();
			mApn.id = cr.getString(cr.getColumnIndex(ID));
			mApn.apn = cr.getString(cr.getColumnIndex(APN));
			mApn.numeric = cr.getString(cr.getColumnIndex(NUMERIC));
			mApn.name = cr.getString(cr.getColumnIndex(NAME));
		}

		if (cr != null) {
			cr.close();
		}

		return mApn;
	}

	private int getCurrentApnId() {

		int id = -1;

		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(CURRENT_APN_URI, null, null, null, null);
		while (cursor.moveToNext()) {
			id = cursor.getInt(0);
		}

		if (cursor != null) {
			cursor.close();
		}
		return id;
	}
	
	private void initApnList() {
		MyPreference preference = new MyPreference(mContext);		
		String apnStr = preference.getString(MyPreference.KEY_APN_LIST);
		
		if (apnStr == "null") {
			apnStr = "[{\"carrier\":\"骞垮窞鐗╄仈缃�(ChinaUnicom)\",\"mcc\":\"460\",\"mnc\":\"06\",\"apn\":\"UNIM2M.GZM2MAPN\"},"
					+ "{\"carrier\":\"鍗椾含鐗╄仈缃�(ChinaUnicom)\",\"mcc\":\"460\",\"mnc\":\"06\",\"apn\":\"UNIM2M.NJM2MAPN\"},"
					+ "{\"carrier\":\"璇鐗╄仈缃�(ChinaUnicom)\",\"mcc\":\"460\",\"mnc\":\"06\",\"apn\":\"zjjxyjqc01.clfu.njm2mapn\"}]";
		}

		try {
			apnList.clear();
			JSONArray arr = new JSONArray(apnStr);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);

				APN apn = new APN();
				apn.numeric = obj.getString(MCC) + obj.getString(MNC);
				apn.apn = obj.getString(APN);
				apn.name = obj.getString("carrier");
				apnList.add(apn);
			}
		} catch (JSONException e) {		
			e.printStackTrace();
		}
	}
	private void saveApnList(){
		try{
			JSONArray arr = new JSONArray();
			for (int i = 0; i < apnList.size(); i++){
				APN apn = apnList.get(i);
				JSONObject obj = new JSONObject();
				arr.put(obj);
				obj.put(MCC, apn.numeric.substring(0, 3));
				obj.put(MNC, apn.numeric.substring(3));
				obj.put(APN, apn.apn);
				obj.put("carrier", apn.name);			
			}
			String s = arr.toString();			
			MyPreference preference = new MyPreference(mContext);
			preference.set(MyPreference.KEY_APN_LIST, s);
		} catch (JSONException e){
			e.printStackTrace();
		}
	}
	private void changeCurrentApn() {		
		APN currentApn = getCurrentApnInfo();
		int result = isInApnList(apnList, currentApn);

		if (result == -1) {
			setApn(apnList.get(0));
		} else if (result + 1 >= apnList.size()) {
			setApn(apnList.get(0));
		} else {
			setApn(apnList.get(result + 1));
		}
	}

	private int isInApnList(List<APN> apnList, APN apn) {
		for (int i = 0; i < apnList.size(); i++) {
			if (isSameApn(apnList.get(i), apn)) {
				return i;
			}
		}
		return -1;
	}

	private boolean isSameApn(APN apn1, APN apn2) {

		if (!apn1.name.equals(apn2.name)) {
			return false;
		}
		if (!apn1.numeric.equals(apn2.numeric)) {
			return false;
		}	
		return true;
	}	

	private void setApn(APN mApn) {
		if (mApn == null) {
			return;
		}
		if (mApn.numeric.length() != 5) {
			return;
		}

		String mcc = mApn.numeric.substring(0, 3);
		String mnc = mApn.numeric.substring(3, 5);
		String apn = mApn.apn;
		String name = mApn.name;

		Intent apnIntent = new Intent("ACTION_IME_SET_APN");
		apnIntent.putExtra("mnc", mnc);
		apnIntent.putExtra("mcc", mcc);
		apnIntent.putExtra("apn", apn);
		apnIntent.putExtra("name", name);
		Log.i(TAG, "setApn : " + name + "," + mcc + "," + mnc + "," + apn);
	}

	private boolean isSimRight() {
		for (int i = 0; i < apnList.size(); i++) {
			APN iApn = apnList.get(i);
			try{
			if (mTelephonyMgr.getSimSerialNumber().startsWith(iApn.numeric)) {
				return true;
			}
			}catch(NullPointerException e){
				
			}
		}
		return false;
	}

	public boolean update(APN apn) {
		if (!mTelephonyMgr.getSimSerialNumber().startsWith(apn.numeric)) {
			return false;
		}
		if(-1 == isInApnList(apnList, apn)){
			apnList.add(0, apn);
			saveApnList();			
		}else{
			return false;
		}
		return true;
	}
	
	public APN newApn(){
		return new APN();
	}
	
	public class APN {
		String id;
		String apn;
		String numeric;	
		String name;
		String user;
		String pass;
		String netid;
		String ip;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		public String getPass() {
			return pass;
		}
		public void setPass(String pass) {
			this.pass = pass;
		}
		public String getNetid() {
			return netid;
		}
		public void setNetid(String netid) {
			this.netid = netid;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
	}
}
