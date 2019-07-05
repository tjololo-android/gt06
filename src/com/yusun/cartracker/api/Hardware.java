package com.yusun.cartracker.api;

import java.util.Date;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.helper.Utils;
import com.yusun.cartracker.model.Fence;
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
	private int mLanguage = 0x01;				//NG
	private TimeZone mTimeZone = new TimeZone();//NG
	private String DEVICETYPE = "11";			//NG
	private int mAlarmType = 0;					//NG
	private int mAlarmIndex = 0;				//NG
	private int mFenceType = 0;					//NG
    private Fence mFence;
    private int mSensorInterval;
    private int mSendsTimeout;
    private int mDefenseDelay;    
	private int mGpsInterval = 30;				
	private int mLbsInterval = 30;				
	private int mGpsWorkInterval = 30;			
	private boolean mGpsFixed = false;			//NG
	private String mIp;
	private String mPort;
	private String mSosNumber;
    private boolean mOilPowerControl;
    private boolean mGpsPower;
    private String mPassword;
    private String mAdminPassword;
    private String mTimeZoneEast="E";
    private int mTimeZoneNum=8;   
    private boolean mMonitor;
    private boolean mVirbation;   
    private boolean mRecharge;
    private boolean mAcc;
    private String mGpsAnalyseUrl;
    private int mFenceNum;
    
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
		return mTimeZone;
	}
	public int getLanguage() {
		return mLanguage;
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
	public boolean getVibration(){
		return mVirbation;
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
		logger.info("imei="+IMEI+"\nIMSI="+IMSI+"\nICCID="+ICCID+"\nCID="+CID+" LAC="+LAC+" MCC="+MCC+" MNC="+MNC);		
		initSettings();
	}
	private void initSettings(){
		mMyPreference = new MyPreference(mContext);
		mIp = mMyPreference.getString(MyPreference.KEY_IP);
		mPort = mMyPreference.getString(MyPreference.KEY_PORT);
		mGpsInterval = mMyPreference.getInt(MyPreference.KEY_GPS_INTERVAL);
		mLbsInterval = mMyPreference.getInt(MyPreference.KEY_LBS_INTERVAL);
		mGpsWorkInterval = mMyPreference.getInt(MyPreference.KEY_GPS_WORK_INTERVAL);
		mSosNumber = mMyPreference.getString(MyPreference.KEY_SOS_NUM);
		mSensorInterval = mMyPreference.getInt(MyPreference.KEY_SENSOR_TIME);
		mDefenseDelay = mMyPreference.getInt(MyPreference.KEY_ALARM_TIME);
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
	public int getGpsInterval() {
		return mGpsInterval;
	}
	public void setGpsInterval(int interval) {
		if(interval != mGpsInterval){			
			mGpsInterval = interval;
			mMyPreference.set(MyPreference.KEY_GPS_INTERVAL, interval);
		}
	}
	public int getLbsInterval() {
		return mLbsInterval;
	}
	public void setLbsInterval(int interval) {
		if(interval != mLbsInterval){						
			mLbsInterval = interval;
			mMyPreference.set(MyPreference.KEY_LBS_INTERVAL, interval);
		}		
	}
	public int getGpsWorkInterval() {
		return mGpsWorkInterval;
	}
	public void setGpsWorkInterval(int interval) {
		if(interval != mGpsInterval){						
			mGpsWorkInterval = interval;
			mMyPreference.set(MyPreference.KEY_GPS_WORK_INTERVAL, interval);
		}
	}
	public boolean setService(String ip, String port) {
		if(mIp.equalsIgnoreCase(ip) && mPort.equalsIgnoreCase(port))
			return false;		
		int p = Integer.parseInt(port);
		if(Utils.isValidVrl(ip) && p > 0 && p < 65535){
			mIp = ip;
			mPort = port;
			mMyPreference.set(MyPreference.KEY_IP, ip);
			mMyPreference.set(MyPreference.KEY_PORT, port);			
			return true;
		}		
		return false;
	}
	public String getIp(){
		return mIp;
	}
	public String getPort(){
		return mPort;
	}
	public String getProtocol() {
		return "TCP";								//NG
	}
	public String getSOS() {
		return mSosNumber;
	}

	public void setSOS(String sOS) {
		if(!mSosNumber.equals(sOS)){						
			mSosNumber = sOS;
			mMyPreference.set(MyPreference.KEY_SOS_NUM, mSosNumber);
		}		
	}

	public int getDefenseDelay() {
		return mDefenseDelay;
	}
	
	public int getSensorInterval() {
		return mSensorInterval;
	}

	public void setSensorInterval(int val) {	//NG interval to check vibrator second
		if(mSensorInterval != val){
			mSensorInterval = val;
			mMyPreference.set(MyPreference.KEY_SENSOR_TIME, val);
		}
	}
	public void setSendsTimeout(int val) {	//NG timeout no vibrator to close gps
		mSendsTimeout = val;
	}

	public boolean isGpsPowerOn() {	
		return mGpsPower;
	}
	
	public boolean setGPSAnalyseUrl(String content) {
		mGpsAnalyseUrl = content;
		return false;
	}

	public String getGPSAnalyseUrl() {
		return mGpsAnalyseUrl;
	}

	
	public void setFence(Fence fence) {
		mFence = fence;		//NG		
	}
	public Fence getFence(){
		return mFence;
	}

	public boolean turnOnGps(boolean on) {		//NG
		mGpsPower = on;
		return true;
	}

	public boolean checkPass(String password) {
		return password.equals(mPassword);
	}

	public void modifyPass(String password) {
		mPassword = password;		
	}

	public boolean checkAdminPass(String password) {
		return mAdminPassword.equals(password);
	}

	public void resetPass() {		//NG
				
	}
	public void setTimeZone(String east, int num) {		
		mTimeZoneEast = east;
		mTimeZoneNum = num;
	}
	public void setDefenseDelay(int delay) {		
		if(mDefenseDelay != delay){
			mDefenseDelay = delay;
			mMyPreference.set(MyPreference.KEY_SENSOR_TIME, delay);
		}
	}

	public void switchMonitor() {	//NG
		mMonitor = true;
	}
	public void setVibration(boolean virbation) {	//NG	
		mVirbation = virbation;
	}

	public int getFenceNum() {		
		return mFenceNum;
	}
	public void setFenceNum(int fenceNum) {		
		mFenceNum = fenceNum;
	}

	public void setDate(Date date) {	//NG
		
	}
}
	


