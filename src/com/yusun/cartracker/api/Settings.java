package com.yusun.cartracker.api;

import com.yusun.cartracker.helper.Utils;
import com.yusun.cartracker.model.Fence;

import android.content.Context;

public class Settings {
	Context mContext;
					
	private int mLanguage = 0x01;				//NG
	private String DEVICETYPE = "11";			//NG
    private int mSensorInterval;
    private int mSendsTimeout;
    private int mDefenseDelay;    
	private int mGpsInterval = 30;				
	private int mLbsInterval = 30;				
	private int mGpsWorkInterval = 30;			
	private String mIp;
	private String mPort;
	private String mSosNumber;
    private boolean mGpsPower;
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
	
	public void init(){
		
	}
	
	public String getDeviceType() {
		return DEVICETYPE;
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
		if(interval != mGpsInterval){			
			mGpsInterval = interval;		
		}
	}
	public int getLbsInterval() {
		return mLbsInterval;
	}
	public void setLbsInterval(int interval) {
		if(interval != mLbsInterval){						
			mLbsInterval = interval;			
		}		
	}
	public int getGpsWorkInterval() {
		return mGpsWorkInterval;
	}
	public void setGpsWorkInterval(int interval) {
		if(interval != mGpsInterval){						
			mGpsWorkInterval = interval;			
		}
	}
	public boolean setService(String ip, String port) {
		if(mIp.equalsIgnoreCase(ip) && mPort.equalsIgnoreCase(port))
			return false;		
		int p = Integer.parseInt(port);
		if(Utils.isValidVrl(ip) && p > 0 && p < 65535){
			mIp = ip;
			mPort = port;					
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
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	public Context getContext() {
		return mContext;
	}

	public boolean checkAdminPass(String password) {
		return mAdminPassword.equals(password);
	}

	public void resetPass() {		//NG
				
	}

	public void switchMonitor() {	//NG
		mMonitor = true;
	}
	public void setVibration(boolean virbation) {	//NG	
		mVirbation = virbation;
	}
	public void setDefenseDelay(int delay) {		
		if(mDefenseDelay != delay){
			mDefenseDelay = delay;
		}
	}
	public void setFence(Fence fence) {
		mFence = fence;		//NG		
	}
	public Fence getFence(){
		return mFence;
	}
}
