package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.Waiter;

public abstract class BaseProtocol implements IClientStatus, IProtocol {
	private int mClientStatus = 0;
	int CLIENT_DISCONNECTED = 0;
	int CLIENT_CONNECTED = 1;
	int CLIENT_LOGIN = 2;	
 
	Logger logger = new Logger(BaseProtocol.class);
	
	public void init(){
		logger.info("init+++");
		logger.info("init---");
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
		start();
		mClientStatus = CLIENT_LOGIN;
	}
	@Override
	public void onEcho(int cmd, Object content) {
		if(!Waiter.instance().onEcho(cmd)){
			logger.error("unsupported command "+cmd);
		}
	}
	public boolean isOnline(){
		logger.info("mClientStatus="+mClientStatus);
		return CLIENT_DISCONNECTED != mClientStatus;
	}
}
