package com.yusun.cartracker;

import com.yusun.cartracker.api.ApnSetting;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.sms.abs.CmdManager;
import com.yusun.cartracker.netty.NettyClient;
import com.yusun.cartracker.position.DatabaseHelper;
import com.yusun.cartracker.position.NetworkManager;
import com.yusun.cartracker.protocol.abs.BaseProtocol;
import com.yusun.cartracker.protocol.abs.ProtocolMgr;

import android.content.Context;

public class AppContext{
	Logger logger = new Logger(AppContext.class);
	
	public void init(){		
		logger.info("init+++");		
		if(null == mContext){
			throw new IllegalArgumentException("set context first");
		}
		mApnSetting = new ApnSetting(getContext());		
		mSmsCmdManager = new CmdManager();
		mSmsCmdManager.init();		

		mProtocol = ProtocolMgr.getProtocol();
		mProtocol.init();
		
		mAlarmMgr = new AlarmMgr();
		mAlarmMgr.init();
		
		mMotionMgr = new MotionMgr(mContext);
		mMotionMgr.start();
		
		logger.info("init---");
	}
	private MotionMgr mMotionMgr;
	public void uninit(){
		logger.info("uninit+++");

		if(null != mAlarmMgr){
			mAlarmMgr.uninit();
		}
		
		if(null != mProtocol){
			mProtocol.uninit();
		}
		if(null != mSmsCmdManager){
			mSmsCmdManager.uninit();
		}
		if(null != mMotionMgr){
			mMotionMgr.stop();
		}
		logger.info("uninit---");
	}
	
	private AlarmMgr mAlarmMgr;
	public AlarmMgr getAlarmMgr(){
		return mAlarmMgr;
	}
	
	private CmdManager mSmsCmdManager;
	public CmdManager getSmsCmdManager(){
		return mSmsCmdManager;
	}
	
	private ApnSetting mApnSetting;
	public ApnSetting getmApnSetting() {
		return mApnSetting;
	}	
	private AppContext(){
		
	}	
	private BaseProtocol mProtocol;
	public BaseProtocol getProtocol() {
		return mProtocol;
	}	
	private static AppContext _this;
	public static AppContext instance(){
		if(null == _this){
			_this = new AppContext();			
		}
		return _this;
	}
	
	private NettyClient mClient;
	public void setClient(NettyClient client){
		mClient = client;
	}
	public NettyClient getClient() {
		return mClient;
	}
	
	private Context mContext;
	public void setContext(Context context) {
		mContext = context;
	}
	public Context getContext(){
		return mContext;
	}
	private DatabaseHelper mDatabaseHelper;
	public DatabaseHelper getDatabaseHelper() {
		return mDatabaseHelper;
	}
	public void setDatabaseHelper(DatabaseHelper database){
		mDatabaseHelper = database;
	}

	public void resetNetServer() {
		if(null != mClient){
			mClient.stop();
			mClient.start();
		}
	}
	
	private NetworkManager mNetworkManager;
	public void setNetWorkManager(NetworkManager networkManager){
		mNetworkManager = networkManager;
	}
	public boolean isNetworkConnected() {
		if(null != mNetworkManager){
			return mNetworkManager.isOnline(); 
		}
		return false;
	}
}