package com.yusun.cartracker.api;

import java.util.Date;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.helper.Utils;
import com.yusun.cartracker.model.TimeZone;
import com.yusun.cartracker.position.Position;

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
    private String IMEI="860016020783556";
	private String ICCID="460060276069992";
	private String IMSI="460060276069992";
	private String MCC="460";
	private String MNC="06";
	private int mSignal = 10;	
	private int LAC = 9514;
	private int CID = 101815812;	
	private int PRETIME=10;		
	private int mBattery = 10;					//NG
	private TimeZone mTimeZone = new TimeZone();//NG
	private int mAlarmType = 0;					//NG
	private int mAlarmIndex = 0;				//NG
	private int mFenceType = 0;					//NG    
	private boolean mGpsFixed = false;			//NG
    private boolean mOilPowerControl;
    private String mTimeZoneEast="E";
    private int mTimeZoneNum=8;   
    private boolean mRecharge;
    private boolean mAcc;
    private int mFenceNum;
    private Position mLastPos;
    private SystemSetting mSystemSetting;
    
	private Hardware(){}
	private static Hardware _this;
	public static Hardware instance(){
		if(null == _this){
			_this = new Hardware();
		}
		return _this;
	}
	
	public TimeZone getTimeZone() {
		return mTimeZone;
	}
	public void rebootOnTime() {	//NG
		
	}
	public void rebootAfter1Minute() {	//NG
		
	}	
	public void factory() {	//NG
		
	}
	public boolean getOilPowerControl(){
		return mOilPowerControl;
	}
	public void setOilPowerControl(boolean control){
		mOilPowerControl = control;	//NG
	}	
	public boolean getGpsFixed(){
		return mGpsFixed;
	}
	public void setGpsFixed(boolean isFixed){
		mGpsFixed = isFixed;
	}
	public boolean getRecharge(){
		return mRecharge;
	}
	public void setRecharge(boolean recharge){
		mRecharge = recharge;
	}
	public boolean getAcc(){
		return mAcc;
	}
	public void setAcc(boolean acc){
		mAcc = acc;
	}
	public String getTimeZone2() {
		return mTimeZoneEast+mTimeZoneNum;
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
		if(Utils.isFeatures()){
			IMEI="860016020783556";
			CID=102181894;
			LAC=42303;
			MCC="460";
			MNC="01";
		}
		mSystemSetting = new SystemSetting();
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
				mSignal = signalStrength.getGsmSignalStrength();
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
				mBattery = (level * 100) / scale;				
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
		return mSignal;
	}
	public int getLAC() {
		return LAC;
	}
	public int getCID() {
		return CID;
	}
	public int getBATTERY() {
		return mBattery;
	}
	public int getPreTime() {		
		return PRETIME;
	}
	public void resetAlarmType() {		
		mAlarmType = 0;
	}
	public void setAlarmType(int alarmType) {		
		mAlarmType |= alarmType;
	}
	public int getAlarmType() {		
		return mAlarmType;
	}
	public int getAlarmIndex() {
		return mAlarmIndex;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	public Context getContext() {
		return mContext;
	}
	public int getFenceType() {		
		return mFenceType;
	}
	public void setTimeZone(String east, int num) {		
		mTimeZoneEast = east;
		mTimeZoneNum = num;
	}

	public int getFenceNum() {		
		return mFenceNum;
	}
	public void setFenceNum(int fenceNum) {		
		mFenceNum = fenceNum;
	}

	public void setDate(Date date) {	//NG
		
	}

	public String getCenterNum() {		//NG
		return "0000000000000";
	}

	public String getMode() {	//NG
		return "mode 0,1,200";
	}
	
	public Position getLastPos(){
		return mLastPos;
	}
	public void setLastPos(Position pos){
		mLastPos = pos;
	}
	
	private boolean mGpsPower;
	public boolean turnOnGps(boolean on) {
		mGpsPower = on;	
		mSystemSetting.turnOnGps(on);
		return true;
	}
	public boolean isGpsPowerOn() {	
		return mGpsPower;
	}	
	
	final private class SystemSetting{
	    public static final String ACTION_AIRPLANE_MODE_SET = "com.yusun.service.intent.ACTION_AIRPLANE_MODE_SET"; 
	    public static final String ACTION_REBOOT_SYSTEM = "com.yusun.service.intent.action.ACTION_REBOOT_SYSTEM";    
	    public static final String ACTION_SET_GPS_STATUS = "com.yusun.service.intent.action.ACTION_SET_GPS_STATUS"; 
	    public static final String ACTION_SET_SYSTEM_SETTINGS = "com.yusun.service.intent.action.ACTION_SET_SYSTEM_SETTINGS";
	    public static final String ACTION_DATA_MODE_SET = "com.yusun.service.intent.ACTION_DATA_MODE_SET";
	    public static final String ACTION_YUSUN_SET_APN = "com.yusun.intent.action.ACTION_SET_APN"; 
	    public static final String ACTION_RECORY_FACTORY = "com.yusun.service.intent.action.ACTION_RECORY_FACTORY";
	    public static final String ACTION_GOTO_SLEEP = "com.yusun.service.intent.action.ACTION_GOTOSLEEP";    
		public static final String ACTION_SET_SYSTEM_TIME = "com.yusun.service.intent.action.ACTION_SET_SYSTEM_TIME";
		
		public void turnOnGps(boolean on){
			Intent intent = new Intent(ACTION_SET_GPS_STATUS);
		    intent.putExtra("on", true);
		    sendBroadcast(intent);
		}
		public void turnOnAirMode(boolean on){
			Intent intent = new Intent(ACTION_AIRPLANE_MODE_SET);
		    intent.putExtra("on", true);
		    sendBroadcast(intent);
		}
		public void truenOnDataConnection(boolean on){
			Intent intent = new Intent(ACTION_DATA_MODE_SET);
		    intent.getBooleanExtra("on", true);
		    sendBroadcast(intent);
		}
		public void rebootSystem(boolean on){
			Intent intent = new Intent(ACTION_REBOOT_SYSTEM);
		    sendBroadcast(intent);
		}
		public void resetFactory(){
			Intent intent = new Intent(ACTION_RECORY_FACTORY);
		    sendBroadcast(intent);
		}		
		public void gotoSleep(){
			Intent intent = new Intent(ACTION_GOTO_SLEEP);
		    sendBroadcast(intent);
		}
		public void setSystemTime(int[] time){
			Intent intent = new Intent(ACTION_SET_SYSTEM_TIME);
		    intent.putExtra("time", time);
		    sendBroadcast(intent);
		}
		public void setSystemSetting(String name, int val, String str){
			Intent intent = new Intent(ACTION_SET_SYSTEM_SETTINGS);
		    intent.putExtra("name", name);
		    if(-1 != val){
		      intent.putExtra("intParam", val);
		    }else{
		      intent.putExtra("stringParam", str);
		    }			
		    sendBroadcast(intent);
		}
		public void setApn(String mnc, String mcc, String apn, String name){
			Intent intent = new Intent(ACTION_YUSUN_SET_APN);
			 intent.putExtra("mnc", mnc);
		     intent.putExtra("mcc", mcc);
		     intent.putExtra("apn", apn);
		     intent.putExtra("name", name);
		    sendBroadcast(intent);
		}		
	}
	public void sendBroadcast(Intent intent){
		mContext.sendBroadcast(intent);
	}
}