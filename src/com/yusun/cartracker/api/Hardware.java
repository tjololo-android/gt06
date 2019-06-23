package com.yusun.cartracker.api;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.TimeZone;

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
	Logger logger = new Logger(Hardware.class);
	private Hardware(){}
	private static Hardware _this;
	public static Hardware instance(){
		if(null == _this){
			_this = new Hardware();
		}
		return _this;
	}
	
	public String getDeviceType() {
		return DEVICETYPE;
	}
	public TimeZone getTimeZone() {
		return TIMEZONE;
	}
	public int getLanguage() {
		return LANGUAGE;
	}
	public static void rebootOnTime() {
		
	}	
	public static boolean isElectronicOn(){
		return true;
	}
	public boolean isGpsFixed(){
		return GPS_FIXED;
	}
	public void setGpsFixed(boolean isFixed){
		GPS_FIXED = isFixed;
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
		if(null == tel){
			logger.error("get telephony manager fail!");
			return;
		}
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
		IMEI="860016020783556";	//NG
		IMSI = tel.getSubscriberId();
		ICCID = tel.getLine1Number();
		if(null != IMSI && IMSI.length() > 5){
			MCC = IMSI.substring(0, 3);
			MNC = IMSI.substring(3, 5);
		}
		installPhoneStateListener(tel);
		installBatteryStatus();
		logger.info("imei="+IMEI+"\nIMSI="+IMSI+"\nICCID="+ICCID+"\nCID="+CID+" LAC="+LAC+" MCC="+MCC+" MNC="+MNC);
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
		return IMEI;
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
	public int getPreTime() {		
		return PRETIME;
	}	
	public int getAlarmType() {		
		return ALARM_TYPE;
	}
	public int getAlarmIndex() {
		return ALARM_INDEX;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	public Context getContext() {
		return mContext;
	}
	public int getAlarmFence() {		
		return ALARM_FENCE;
	}
	public int getGpsInterval() {
		return GPS_INTERVAL;
	}
	public int getLbsInterval() {
		return LBS_INTERVAL;
	}
	private String IMEI="860016020783556";
	private String ICCID="460060276069992";
	private String IMSI="460060276069992";
	private String MCC="460";
	private String MNC="06";
	private int SIGNAL = 10;	
	private int LAC = 9514;
	private int CID = 101815812;
	private int BATTERY = 10;
	private int PRETIME=10;						//NG
	private int LANGUAGE = 0x01;				//NG
	private TimeZone TIMEZONE = new TimeZone();	//NG
	private String DEVICETYPE = "11";			//NG
	private int ALARM_TYPE = 0;					//NG
	private int ALARM_INDEX = 0;				//NG
	private int ALARM_FENCE = 0;				//NG
	private int GPS_INTERVAL = 30;				//NG
	private int LBS_INTERVAL = 30;				//NG
	private boolean GPS_FIXED = false;			//NG
}
	


