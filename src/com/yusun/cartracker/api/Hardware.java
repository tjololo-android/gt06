package com.yusun.cartracker.api;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.helper.Utils;
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
	MyPreference mMyPreference;
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
	public static boolean isOilPowerControl(){
		return true;					//NG
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
	
	public String getTimeZone2() {	//NG
		return "E8";
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
		IMSI = tel.getSubscriberId();
		ICCID = tel.getLine1Number();
		if(null != IMSI && IMSI.length() > 5){
			MCC = IMSI.substring(0, 3);
			MNC = IMSI.substring(3, 5);
		}
		installPhoneStateListener(tel);
		installBatteryStatus();
		logger.info("imei="+IMEI+"\nIMSI="+IMSI+"\nICCID="+ICCID+"\nCID="+CID+" LAC="+LAC+" MCC="+MCC+" MNC="+MNC);
		
		initSettings();
	}
	private void initSettings(){
		mMyPreference = new MyPreference(mContext);
		IP = mMyPreference.getString(MyPreference.KEY_IP);
		PORT = mMyPreference.getString(MyPreference.KEY_PORT);
		GPS_INTERVAL = mMyPreference.getInt(MyPreference.KEY_GPS_INTERVAL);
		LBS_INTERVAL = mMyPreference.getInt(MyPreference.KEY_LBS_INTERVAL);
		GPS_WORK_INTERVAL = mMyPreference.getInt(MyPreference.KEY_GPS_WORK_INTERVAL);
		SOS = mMyPreference.getString(MyPreference.KEY_SOS_NUM);
		Sensor_Time = mMyPreference.getInt(MyPreference.KEY_SENSOR_TIME);
		Alarm_Time = mMyPreference.getInt(MyPreference.KEY_ALARM_TIME);
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
	public void setGpsInterval(int interval) {
		if(interval != GPS_INTERVAL){			
			GPS_INTERVAL = interval;
			mMyPreference.set(MyPreference.KEY_GPS_INTERVAL, interval);
		}
	}
	public int getLbsInterval() {
		return LBS_INTERVAL;
	}
	public void setLbsInterval(int interval) {
		if(interval != LBS_INTERVAL){						
			LBS_INTERVAL = interval;
			mMyPreference.set(MyPreference.KEY_LBS_INTERVAL, interval);
		}		
	}
	public int getGpsWorkInterval() {
		return GPS_WORK_INTERVAL;
	}
	public void setGpsWorkInterval(int interval) {
		if(interval != GPS_INTERVAL){						
			GPS_WORK_INTERVAL = interval;
			mMyPreference.set(MyPreference.KEY_GPS_WORK_INTERVAL, interval);
		}
	}
	public boolean setService(String ip, String port) {
		if(IP.equalsIgnoreCase(ip) && PORT.equalsIgnoreCase(port))
			return false;		
		int p = Integer.parseInt(port);
		if(Utils.isValidVrl(ip) && p > 0 && p < 65535){
			IP = ip;
			PORT = port;
			mMyPreference.set(MyPreference.KEY_IP, ip);
			mMyPreference.set(MyPreference.KEY_PORT, port);			
			return true;
		}		
		return false;
	}
	public String getIp(){
		return IP;
	}
	public String getPort(){
		return PORT;
	}
	public String getProtocol() {
		return "TCP";								//NG
	}
	public String getSOS() {
		return SOS;
	}

	public void setSOS(String sOS) {
		if(!SOS.equals(sOS)){						
			SOS = sOS;
			mMyPreference.set(MyPreference.KEY_SOS_NUM, SOS);
		}		
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
	private int GPS_INTERVAL = 30;				
	private int LBS_INTERVAL = 30;				
	private int GPS_WORK_INTERVAL = 30;			
	private boolean GPS_FIXED = false;			//NG
	private String IP;
	private String PORT;
	private String SOS;
	private int Alarm_Time;
	public int getAlarm_Time() {
		return Alarm_Time;
	}

	public void setAlarm_Time(int val) {
		if(Alarm_Time != val){
			Alarm_Time = val;
			mMyPreference.set(MyPreference.KEY_SENSOR_TIME, val);
		}
	}

	public int getSensor_Time() {
		return Sensor_Time;
	}

	public void setSensor_Time(int val) {
		if(Sensor_Time != val){
			Sensor_Time = val;
			mMyPreference.set(MyPreference.KEY_SENSOR_TIME, val);
		}
	}
	private int Sensor_Time;
	public String getGpsAddressAnalyser() {
		return "unknown";			//NG
	}

	public boolean isGpsPowerOn() {	
		return true;				//NG
	}
}
	


