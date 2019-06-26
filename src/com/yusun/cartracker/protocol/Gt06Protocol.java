package com.yusun.cartracker.protocol;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.SmsSender;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.Task;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.model.TimerTask;
import com.yusun.cartracker.position.Position;
import com.yusun.cartracker.protocol.abs.BaseProtocol;

import io.netty.channel.Channel;

public class Gt06Protocol extends BaseProtocol{
	Logger logger = new Logger(Gt06Protocol.class);
	
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
		super.init();
		logger.info("init+++");	
		mTaskMgr.reg(TaskLogin);
		mTaskMgr.reg(TaskHeartbeat);	
		mCmdMgr.reg(CmdAlarmCh);
		mCmdMgr.reg(CmdAlarmEn);
		logger.info("init---reg task end");
	}	
	@Override
	public void uninit(){
		logger.info("uninit+++");
		mTaskMgr.unreg();
		logger.info("uninit---");
	}
	@Override
	public void start() {
		logger.info("start+++");
		onLogin();						
		mTaskMgr.post(TaskHeartbeat);
		startReportPosition();
		logger.info("start---");		
	}
	@Override
	public void stop() {
		logger.info("stop+++");
		stopReportPosition();
		mTaskMgr.stop();
		mCmdMgr.stop();		
		logger.info("stop---");
	}
	@Override
	public void login() {		
		mTaskMgr.post(TaskLogin);
	}
	
	TimerTask TaskLogin = new TimerTask(Gt06ProtocolConstant.MSG_LOGIN) {
		int MAX_COUNT = 3;
		int count = 0;
		int TIME_OUT = 5000;
		public Message getMessage() {
			setTimeOut(TIME_OUT);	
			MessageLogin msg = new MessageLogin(getId());
			msg.DeviceType = Hardware.instance().getDeviceType();
			msg.Language = Hardware.instance().getLanguage();
			return msg;
		}

		public void onComplete(int result) {
			if(TaskMgr.RESULT_SUCESS == result){
				count = 0;
				start();
			}else{
				if(++count > MAX_COUNT){
					Hardware.rebootOnTime();
				}else{
					
				}
			}
		}
	};	

	TimerTask TaskHeartbeat = new TimerTask(Gt06ProtocolConstant.MSG_HEARTBEAT) {
		int PERIOD = 3*60*1000;
		int MAX_COUNT = 3;
		int count = 0;
		int TIME_OUT = 5000;

		public Message getMessage() {
			setTimeOut(TIME_OUT);
			MessageHeartbeat msg = new MessageHeartbeat(getId());	
			return msg;
		}

		@Override
		public void onComplete(int result) {
			if(TaskMgr.RESULT_SUCESS == result){
				count = 0;
				mTaskMgr.postDelayed(this, PERIOD);;
			}else{
				if(++count > MAX_COUNT){
					Hardware.rebootOnTime();
				}
			}
		}
	};	
	
	static boolean isReportPos = true;
	void startReportPosition(){
		isReportPos = true;
    	new Thread(){
    		public void run(){
    			MessageLbs lbs = new MessageLbs(Gt06ProtocolConstant.MSG_LBS_MULTIPLE);
    			mTaskMgr.sendMessage(lbs);
    			MessageWifi wifi = new MessageWifi(Gt06ProtocolConstant.MSG_LBS_WIFI);
    			mTaskMgr.sendMessage(wifi);
    			while(isReportPos){
    				if(Hardware.instance().isGpsFixed()){
    					Position pos = AppContext.instance().getDatabaseHelper().selectPosition();
    	    			if(null != pos){
    	    		    	MessagePosition msg = new MessagePosition(Gt06ProtocolConstant.MSG_GPS_LBS_2, pos);
    	    		    	mTaskMgr.sendMessage(msg);
    	    				AppContext.instance().getDatabaseHelper().deletePosition(pos.getId());
    	    			}
    	    			mysleep(Hardware.instance().getGpsInterval());
    				}else{
    					mysleep(Hardware.instance().getLbsInterval());
    					lbs = new MessageLbs(Gt06ProtocolConstant.MSG_LBS_MULTIPLE);
    	    			mTaskMgr.sendMessage(lbs);    	    			
    				}
    			}
    		}
    	}.start();    	
	}
	
	void stopReportPosition(){
		isReportPos = false;
	}
	
	void mysleep(int sec){
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void alarm(){
//    	int fence = Hardware.instance().getAlarmFence();
//    	Position pos = Hardware.instance().getposition();
//    	Message msg = null;
//    	if(0 == fence){
//    		msg = new MessageFence(Gt06ProtocolConstant.MSG_GPS_LBS_STATUS_2, pos);
//    	}else{
//    		msg = new MessageFence(Gt06ProtocolConstant.MSG_GPS_LBS_STATUS_3, pos);
//    	}
//    	msg.sendToTarget();
    }
    
    Task CmdAlarmCh = new Task(Gt06ProtocolConstant.MSG_WIFI){//NG	//6.gps报警包，中文回复
    	int serviceFlag;
    	String AlarmSMS;
    	String Address;
    	String phoneNum;
		@Override
		public void run() {
			
			
		}

		@Override
		public void onComplete(int result) {
			// TODO Auto-generated method stub
			
		}
    	
		void decode(){
			
		}
    };
    Task CmdAlarmEn = new Task(Gt06ProtocolConstant.MSG_ADDRESS_RESPONSE){//NG  //6.gps报警包，英文回复
    	int serviceFlag;
    	String AlarmSMS;
    	String Address;
    	String phoneNum;
		@Override
		public void run() {
			
			
		}

		@Override
		public void onComplete(int result) {
			// TODO Auto-generated method stub
			
		}
    	
		void decode(){
			
		}
    };
    
    
    TimerTask taskAlarmLbs = new TimerTask(Gt06ProtocolConstant.MSG_LBS_STATUS){//NG //7.LBS报警包
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };   
    
    TimerTask taskRequestGps = new TimerTask(Gt06ProtocolConstant.MSG_ADDRESS_REQUEST){//NG //8.请滶gps地址
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };     
    
    TimerTask taskRequestLbs = new TimerTask(Gt06ProtocolConstant.MSG_WIFI){//NG //9.请滶LBS地址
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };  
    
    Task CmdOnline = new TimerTask(Gt06ProtocolConstant.MSG_COMMAND_0){//NG //10.在线指令
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    }; 
    
 
    
    TimerTask ExeResult = new TimerTask(Gt06ProtocolConstant.MSG_STRING_INFO){//NG //10.指令执行结果回复
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };   
    
    TimerTask AdjustTime = new TimerTask(Gt06ProtocolConstant.MSG_TIME_REQUEST){//NG //11.校时包
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };
    Task AdjustTimeEcho = new TimerTask(Gt06ProtocolConstant.MSG_TIME_REQUEST){//NG //11.校时包
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };
    
    Task TaskGeneral = new TimerTask(Gt06ProtocolConstant.MSG_INFO){//NG //12.通用信息包
    	int PERIOD = 30000;
		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			return new AlarmLBS(getId());			
		}    	
    };
}
