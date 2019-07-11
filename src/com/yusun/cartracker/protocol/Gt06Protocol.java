package com.yusun.cartracker.protocol;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.model.Waiter;
import com.yusun.cartracker.model.sms.abs.MSG;
import com.yusun.cartracker.position.Position;
import com.yusun.cartracker.protocol.abs.BaseProtocol;

import io.netty.channel.Channel;

public class Gt06Protocol extends BaseProtocol{
	Logger logger = new Logger(Gt06Protocol.class);
	private static volatile boolean mIsRunning = false;
	
	@Override
	public void onInitChannel(Channel sc) {
		logger.info("onInitChanne+++");
		sc.pipeline().addLast(new Gt06FrameDecoder());
		sc.pipeline().addLast(new Gt06ProtocolDecoder());
		sc.pipeline().addLast(new Gt06ProtocolEncoder());
		logger.info("onInitChanne---");
	}
	@Override
	public void init(){		
		logger.info("init+++");	
	}	
	@Override
	public void uninit(){
		logger.info("uninit+++");
	}
	@Override
	public void start() {
		logger.info("start+++");
		mIsRunning = true;
		startReportPosition();
		startHeartbeat();
		logger.info("start---");		
	}
	@Override
	public void stop() {
		logger.info("stop+++");
		mIsRunning = false;
		logger.info("stop---");
	}
	@Override
	public void login() {
		final int MAX_COUNT = 3;		
		final int TIME_OUT = 5000;		
		new Thread("login"){
    		public void run(){
				int count = 0;		
				while(count++ < MAX_COUNT){
					MessageLogin msg = new MessageLogin();
					msg.sendToTarget();
					if(Waiter.instance().wait(msg.getId(), TIME_OUT)){
						onLogin();
						return;
					}
				}
				Hardware.instance().rebootOnTime();
    		}
		}.start();
	}
	void startHeartbeat(){
		final int MAX_COUNT = 3;		
		final int TIME_OUT = 5000;
		final int SLEEP_TIME = 30 * 1000;
		new Thread("heartbeat"){
    		public void run(){
    			int count = 0;
				while(mIsRunning && count++ < MAX_COUNT){
					mysleep(SLEEP_TIME);
					MessageLogin msg = new MessageLogin();
					msg.sendToTarget();
					if(Waiter.instance().wait(msg.getId(), TIME_OUT)){
						count = 0;
					}
				}
				Hardware.instance().rebootOnTime();
    		}
		}.start();
	}		
	void startReportPosition(){	
    	new Thread("reportPos"){
    		public void run(){
    			MessageLbs lbs = new MessageLbs(Gt06ProtocolConstant.MSG_LBS_MULTIPLE);
    			lbs.sendToTarget();
    			MessageWifi wifi = new MessageWifi(Gt06ProtocolConstant.MSG_LBS_WIFI);
    			wifi.sendToTarget();
    			while(mIsRunning){
    				if(Hardware.instance().getGpsFixed()){
    					Position pos = getPosition();
    	    			if(null != pos){
    	    		    	MessagePosition msg = new MessagePosition(Gt06ProtocolConstant.MSG_GPS_LBS_2, pos);
    	    		    	msg.sendToTarget();
    	    				AppContext.instance().getDatabaseHelper().deletePosition(pos.getId());
    	    			}
    	    			mysleep(Settings.instance().getGpsInterval());
    				}else{
    					mysleep(Settings.instance().getLbsInterval());
    					lbs = new MessageLbs(Gt06ProtocolConstant.MSG_LBS_MULTIPLE);
    	    			lbs.sendToTarget();    	    			
    				}
    			}
    		}
    	}.start();    	
	}
	
	void mysleep(int sec){
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
	@Override
    public void alarm(){
    	int fence = Hardware.instance().getFenceType();
    	Position pos = getPosition();
    	CMessage msg = null;
    	if(!Hardware.instance().getGpsFixed()){
    		msg = new AlarmLBS();
    	}else if(0 == fence){
    		msg = new AlarmSFence(0x26, pos);
    	}else{
    		msg = new AlarmMFence(0x27, pos);
    	}
    	msg.sendToTarget();
    }
    
    public void syncTimeOnline(){
		if(mIsRunning && !Hardware.instance().getGpsFixed()){					
			CMessage msg = new CMessage(0x8A);
			msg.sendToTarget();									
		}
    }
    
	@Override
	public void doCmd(int cmd, Object content) {
		logger.info("doCmd cmd="+cmd);
		switch(cmd){
		case Gt06ProtocolConstant.CMD_REQUEST_ADDRESS:		
			MSG sms = (MSG)content;
			CMessage msg = null;
			if(Hardware.instance().getGpsFixed()){
				msg = new MessageRequestPosition(getPosition(), sms.getPhoneNum());
			}else{
				msg = new MessageRequestLbs(sms.getPhoneNum());
			}
			msg.sendToTarget();
			break;
		}
	}
	private Position getPosition() {
		return AppContext.instance().getDatabaseHelper().selectPosition();
	}	
}
