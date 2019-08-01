package com.yusun.cartracker.api;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.helper.Utils;
import com.yusun.cartracker.model.Fence;

import android.content.Context;

public class Settings {
	Context mContext;					
	private int mLanguage;				//NG
	private String mDeviceType;			//NG
    private int mSensorInterval;
    private int mSendsTimeout;
    private int mDefenseDelay;    
	private int mGpsInterval;				
	private int mLbsInterval;				
	private int mGpsWorkInterval;			
	private String mIp;
	private String mPort;
	private String mSosNumber; 
    private String mPassword;
    private String mAdminPassword;
    private boolean mMonitor;
    private boolean mVirbation;   
    private String mGpsAnalyseUrl;
    private Fence mFence;	
    
	private Settings(){}
	private static Settings _this;
	public static Settings instance(){
		if(null == _this){
			_this = new Settings();
		}
		return _this;
	}
	public String getDeviceType() {		
		return mDeviceType;
	}
	public int getLanguage() {
		return mLanguage;
	}
	
	public boolean getVibration(){
		return mVirbation;
	}

	public int getGpsInterval() {
		return mGpsInterval;
	}
	public void setGpsInterval(int interval) {					
		mGpsInterval = interval;		
		update(GPS_INTERVAL, interval);
	}
	public int getLbsInterval() {
		return mLbsInterval;
	}
	public void setLbsInterval(int interval) {								
		mLbsInterval = interval;
		update(LBS_INTERVAL, interval);
	}
	public int getGpsWorkInterval() {
		return mGpsWorkInterval;
	}
	public void setGpsWorkInterval(int interval) {			
		mGpsWorkInterval = interval;			
		update(GPS_WORK_INTERVAL, interval);
	}
	public void setService(String ip, String port) {
		mIp = ip;
		mPort = port;
		update(SERVICE_IP, ip);
		update(SERVICE_PORT, port);
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

	public void setSOS(String sos) {								
		mSosNumber = sos;
		update(SOS_NUMBER, sos);		
	}

	public int getDefenseDelay() {
		return mDefenseDelay;
	}
	
	public int getSensorInterval() {
		return mSensorInterval;
	}

	public void setSensorInterval(int val) {	//NG interval to check vibrator second
		mSensorInterval = val;
		update(SENSOR_INTERVAL, val);
	}
	public void setSendsTimeout(int val) {	//NG timeout no vibrator to close gps
		mSendsTimeout = val;
		update(SENDS_TIMEOUT, val);
	}	
	public boolean setGPSAnalyseUrl(String content) {
		mGpsAnalyseUrl = content;
		update(GPS_ANALYSE_URL, content);
		return false;
	}

	public String getGPSAnalyseUrl() {
		return mGpsAnalyseUrl;
	}
	public boolean checkPass(String password) {
		return password.equals(mPassword);
	}

	public void modifyPass(String password) {
		mPassword = password;
		update(PASSWORD, password);
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	public Context getContext() {
		return mContext;
	}

	public boolean checkAdminPass(String password) {
		return mAdminPassword.equals(password);
	}

	public void resetPass() {
		resetPassword();
		mPassword = (read(PASSWORD));
	}

	public void switchMonitor() {
		mMonitor = true;
		update(MONITOR, mMonitor);
	}
	public void setVibration(boolean virbation) {	//NG
		mVirbation = virbation;
		update(VIRBATION, virbation);
	}
	public void setDefenseDelay(int delay) {		
		mDefenseDelay = delay;
		update(DEFENSE_DELAY, delay);
	}
	public void setFence(Fence fence) {
		mFence = fence;
		update(FENCE, fence);
	}
	public Fence getFence(){
		return mFence;
	}
	private int mCollisionLevel = 5;
	public int getCollisionLevel() {
		return mCollisionLevel;
	}
	public void setCollisionLevel(int collisionLevel) {
		mCollisionLevel = collisionLevel;
	}
	
/******************************************************************************************/
/******************************begin of setting db***************************************/
/******************************************************************************************/
	public static final String LANGUAGE = "LANGUAGE";
    public static final String DEVICETYPE = "DEVICETYPE";
    public static final String SENSOR_INTERVAL = "SENSOR_INTERVAL";
    public static final String SENDS_TIMEOUT = "SENDS_TIMEOUT";
    public static final String DEFENSE_DELAY = "DEFENSE_DELAY";    
    public static final String GPS_INTERVAL = "GPS_INTERVAL";				
    public static final String LBS_INTERVAL = "LBS_INTERVAL";				
    public static final String GPS_WORK_INTERVAL = "GPS_WORK_INTERVAL";			
    public static final String SERVICE_IP = "SERVICE_IP";
    public static final String SERVICE_PORT = "SERVICE_PORT";
    public static final String SOS_NUMBER = "SOS_NUMBER";
    public static final String GPS_POWER = "GPS_POWER";
    public static final String PASSWORD = "PASSWORD";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String MONITOR = "MONITOR";
    public static final String VIRBATION = "VIRBATION";   
    public static final String GPS_ANALYSE_URL = "GPS_ANALYSE_URL";
    public static final String FENCE = "FENCE";
    public void init(){
    	mLanguage = Integer.parseInt(read(LANGUAGE));
    	mDeviceType = (read(DEVICETYPE));
    	mSensorInterval = Integer.parseInt(read(SENSOR_INTERVAL));
    	mSendsTimeout = Integer.parseInt(read(SENDS_TIMEOUT));
    	mDefenseDelay = Integer.parseInt(read(DEFENSE_DELAY));
    	mGpsInterval = Integer.parseInt(read(GPS_INTERVAL));
    	mLbsInterval = Integer.parseInt(read(LBS_INTERVAL));
    	mGpsWorkInterval = Integer.parseInt(read(GPS_WORK_INTERVAL));
    	mIp = (read(SERVICE_IP));
    	mPort = (read(SERVICE_PORT));
    	mSosNumber = (read(SOS_NUMBER));
    	mPassword = (read(PASSWORD));
    	mAdminPassword = (read(ADMIN_PASSWORD));
    	mMonitor = Boolean.parseBoolean(read(MONITOR));
    	mVirbation = Boolean.parseBoolean(read(VIRBATION));
    	mGpsAnalyseUrl = (read(GPS_ANALYSE_URL));
    	mFence = Utils.getFence(read(FENCE)); 
	}
    void update(String key, String val){
    	AppContext.instance().getDatabaseHelper().update(key, val);
    }
    void update(String key, int val){
    	update(key, String.valueOf(val));
    }
    void update(String key, double val){
    	update(key, String.valueOf(val));
    }
    void update(String key, boolean val){
    	update(key, String.valueOf(val));
    }
    void update(String key, Object val){
    	update(key, String.valueOf(val));
    }
    String read(String key){
    	return AppContext.instance().getDatabaseHelper().read(key);
    }
    void resetPassword(){
    	AppContext.instance().getDatabaseHelper().resetPassword();
    }
/******************************************************************************************/
/************************************end of setting db***********************************/
/******************************************************************************************/
}
