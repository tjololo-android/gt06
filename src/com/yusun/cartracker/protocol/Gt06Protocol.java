package com.yusun.cartracker.protocol;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.CmdMgr;
import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.MessageHeartbeat;
import com.yusun.cartracker.model.MessageLogin;
import com.yusun.cartracker.model.MessagePosition;
import com.yusun.cartracker.model.Sender;
import com.yusun.cartracker.model.TimerTask;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.position.DatabaseHelper;
import com.yusun.cartracker.position.Position;
import com.yusun.cartracker.position.TrackingController;
import com.yusun.cartracker.protocol.abs.BaseProtocol;
import com.yusun.cartracker.util.Logger;

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
		mTaskMgr.reg(taskPosition);
		mTaskMgr.reg(TaskHeartbeat);		 
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
		mTaskMgr.post(taskPosition);				
		mTaskMgr.post(TaskHeartbeat);	
		logger.info("start---");		
	}
	@Override
	public void stop() {
		logger.info("stop+++");
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
			msg.DeviceType = Hardware.getDeviceType();
			msg.Language = Hardware.getLanguage();
			return msg;
		}

		public void onComplete(int result) {
			if(TaskMgr.RESULT_SUCESS == result){
				count = 0;
				start();
			}else{
				if(++count > MAX_COUNT){
					Hardware.rebootOnTime();
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
			msg.electronic_b7 = Hardware.isElectronicOn();
			msg.gpsfix_b6 = Hardware.isGpsFixed();
			msg.recharge_b2 = Hardware.isRecharge();
			msg.acc_b1 = Hardware.isAccon();
			msg.guard_b0 = Hardware.isInguard();
			msg.valtage = Hardware.geValtage();
			msg.gsmSignal = Hardware.getSignal();
			msg.language = Hardware.getLanguage();
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
	
    TimerTask taskPosition = new TimerTask(Gt06ProtocolConstant.MSG_GPS_LBS_2){
    	int PERIOD = 30000;
    	Position pos;    	
		@Override
		public void onComplete(int result) {
			if(TaskMgr.RESULT_SUCESS == result){
				delete();
			}
			mTaskMgr.postDelayed(this, PERIOD);
		}
		@Override
		public Message getMessage() {
			read();
			if(null == pos){
				return null;
			}
			MessagePosition msg = new MessagePosition(getId());
			msg.position = pos;
			msg.mcc = Hardware.getmcc();
			msg.mnc = Hardware.getmnc();
			msg.lac = Hardware.getlac();
			msg.cellId = Hardware.getcellId();	
			return msg;
		}
		@Override
		public void run() {
			Message msg = getMessage();
			if(null == msg){
				mTaskMgr.postDelayed(this, 5*60*1000);
			}else{
				mTaskMgr.sendMessage(msg);
			}
		};
		
		void delete(){
			AppContext.instance().getDatabaseHelper().deletePosition(pos.getId());
		}
		void read(){
			pos = AppContext.instance().getDatabaseHelper().selectPosition();
		}
    };
}
