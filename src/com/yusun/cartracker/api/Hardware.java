package com.yusun.cartracker.api;

import com.yusun.cartracker.model.TimeZone;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class Hardware {
	Context mContext;
	private Hardware(){}
	private static Hardware _this;
	public static Hardware instance(){
		if(null == _this){
			_this = new Hardware();
		}
		return _this;
	}
	
	public static String getDeviceType() {	
		return "11";
	}
	public static TimeZone getTimeZone() {
		return null;
	}
	public static int getLanguage() {
		return 1;
	}
	public static void rebootOnTime() {
		
	}	
	public static boolean isElectronicOn(){
		return true;
	}
	public static boolean isGpsFixed(){
		return true;
	}
	public static boolean isRecharge(){
		return true;
	}
	public static boolean isAccon(){
		return true;
	}
	public static boolean isInguard(){
		return true;
	}

	public void init() {
		TelephonyManager tel = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		@SuppressLint("MissingPermission")
		CellLocation cel = tel.getCellLocation();
		if (tel.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
			CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
			CID = cdmaCellLocation.getBaseStationId();
			LAC = cdmaCellLocation.getNetworkId();
		} else {
			GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
			CID = gsmCellLocation.getCid();
			LAC = gsmCellLocation.getLac();
		}
		IMEI = tel.getDeviceId();
		IMSI = tel.getSubscriberId();
		ICCID = tel.getLine1Number();
		MCC = IMSI.substring(0, 3);
		MNC = IMSI.substring(3, 5);
		installPhoneStateListener(tel);
		installBatteryStatus();
	}
	public void uninit(){
		//TelephonyManager tel = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		//tel.listen(null, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		//mContext.unregisterReceiver(arg0);
	}
	void installPhoneStateListener(TelephonyManager tel) {		
		tel.listen(new PhoneStateListener(){
			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				SIGNAL = signalStrength.getGsmSignalStrength();
				super.onSignalStrengthsChanged(signalStrength);
			}
		}, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);		
	}
	void installBatteryStatus() {	
		 IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);		 	
		 mContext.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent intent) {
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				BATTERY = (level * 100) / scale;				
			}
		}, intentFilter);
	}
	
	public String getIMEI() {
		return "860016020783556";
		//return IMEI;
	}
	public String getICCID() {
		return ICCID;
	}
	public String getIMSI() {
		return IMSI;
	}
	public String getMCC() {
		return MCC;
	}
	public String getMNC() {
		return MNC;
	}
	public int getSIGNAL() {
		return SIGNAL;
	}
	public int getLAC() {
		return LAC;
	}
	public int getCID() {
		return CID;
	}
	public int getBATTERY() {
		return BATTERY;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	private String IMEI;
	private String ICCID;
	private String IMSI;
	private String MCC;
	private String MNC;
	private int SIGNAL;	
	private int LAC;
	private int CID;
	private int BATTERY;	
}
	


