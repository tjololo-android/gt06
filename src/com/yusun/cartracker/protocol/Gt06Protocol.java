package com.yusun.cartracker.protocol;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.MessageHeartbeat;
import com.yusun.cartracker.model.MessageLogin;
import com.yusun.cartracker.model.MessagePosition;
import com.yusun.cartracker.model.Task;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.protocol.abs.BaseProtocol;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

public class Gt06Protocol extends BaseProtocol{
	TaskMgr mTaskMgr; 
	@Override
	public void onInitChannel(Channel sc) {
		sc.pipeline().addLast(new Gt06FrameDecoder());
		sc.pipeline().addLast(new Gt06ProtocolDecoder());
		sc.pipeline().addLast(new Gt06ProtocolEncoder());
	}
	@Override
	public void init(){		
		mTaskMgr = AppContext.instance().getmTaskMgr();
		mTaskMgr.reg(TaskLogin);
		mTaskMgr.reg(TaskPosition);
		mTaskMgr.reg(TaskHeartbeat);
	}	
	@Override
	public void start() {
		mTaskMgr.post(TaskLogin);
	}
	@Override
	public void stop() {
				
	}
	
	Task TaskLogin = new Task(Gt06ProtocolConstant.MSG_LOGIN) {
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
				startOtherTask();
			}else{
				if(++count > MAX_COUNT){
					Hardware.rebootOnTime();
				}
			}
		}
	};
	
	public void startOtherTask(){
		mTaskMgr.post(TaskPosition);
		mTaskMgr.post(TaskHeartbeat);
	}

	Task TaskPosition = new Task(Gt06ProtocolConstant.MSG_GPS_LBS_2) {
		int PERIOD = 3*60*1000;
		public Message getMessage() {
			MessagePosition msg = new MessagePosition(getId());
			msg.position = Hardware.getPosition();
			msg.mcc = Hardware.getmcc();
			msg.mnc = Hardware.getmnc();
			msg.lac = Hardware.getlac();
			msg.cellId = Hardware.getcellId();
			return msg;
		}

		@Override
		public void onComplete(int result) {
			mTaskMgr.postDelayed(this, PERIOD);
		}
	};
	Task TaskHeartbeat = new Task(Gt06ProtocolConstant.MSG_HEARTBEAT) {
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

}
