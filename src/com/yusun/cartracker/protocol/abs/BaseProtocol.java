package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.model.CmdMgr;
import com.yusun.cartracker.model.Task;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.util.Logger;

public abstract class BaseProtocol implements IClientStatus, IProtocol {
	private int mClientStatus = 0;
	int CLIENT_DISCONNECTED = 0;
	int CLIENT_CONNECTED = 1;
	int CLIENT_LOGIN = 2;
	
	protected TaskMgr mTaskMgr; 
	protected CmdMgr mCmdMgr;
	Logger logger = new Logger(BaseProtocol.class);
	public void init(){
		logger.info("init+++");
		mTaskMgr = AppContext.instance().getmTaskMgr();
		mCmdMgr = AppContext.instance().getmCmdMgr();
		logger.info("init+++");
	}	
	@Override
	public void onConnected() {
		logger.info("onConnected");
		mClientStatus = CLIENT_CONNECTED;
		login();		
	}
	@Override
	public void onDisconnected() {
		logger.info("onDisconnected");
		mClientStatus = CLIENT_DISCONNECTED;
		stop();		
	}
	@Override
	public void onLogin() {
		logger.info("onLogin");
		mClientStatus = CLIENT_LOGIN;
	}
	@Override
	public void onReceive(int cmd, Object content) {
		Task t = mCmdMgr.get(cmd);
		if(null != t){
			mCmdMgr.post(t);
		}else{
			t = mTaskMgr.get(cmd);
			if(null != t){
				mTaskMgr.onEcho(cmd);
			}else{
				logger.error("receive unknow command="+cmd);
			}
		}
	}
	public boolean isOnline(){
		logger.info("mClientStatus="+mClientStatus);
		return CLIENT_DISCONNECTED != mClientStatus;
	}
}
