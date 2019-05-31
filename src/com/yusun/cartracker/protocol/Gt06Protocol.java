package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.CountDown;
import com.yusun.cartracker.model.CountDown.TimeOutListener;
import com.yusun.cartracker.model.EchoMgr;
import com.yusun.cartracker.model.EchoMgr.EchoListener;
import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.model.MessageHeartbeat;
import com.yusun.cartracker.model.MessageLogin;
import com.yusun.cartracker.model.MessagePosition;
import com.yusun.cartracker.model.Task;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.protocol.abs.BaseProtocol;

import io.netty.channel.socket.SocketChannel;

public class Gt06Protocol extends BaseProtocol{
	SocketChannel sc;
	@Override
	public void addProtocolHandlers(SocketChannel sc) {
		this.sc = sc;
		
		sc.pipeline().addLast(new Gt06FrameDecoder());
		sc.pipeline().addLast(new Gt06ProtocolDecoder());
		sc.pipeline().addLast(new Gt06ProtocolEncoder());
		
		EchoMgr.instance().setListener(Gt06ProtocolConstant.MSG_LOGIN, TaskLogin.getEchoListener());
	}	
	
	void initTask(){
		TaskMgr.instance().add(TaskLogin);
		//TaskMgr.instance().add(TaskPosition);
		//TaskMgr.instance().add(TaskHeartbeat);
	}
	
	Task TaskLogin = new Task() {
		final int TIMEOUT = 5000;
		int mCount = 0;
		final int MAXCOUNT = 3;
		CountDown mCountdown;
		EchoListener mEchoListener; 
		
		@Override
		public void exec() {			
			Message msg = getMessage();
			sc.writeAndFlush(msg);
			mCountdown.start();
		}

		public Message getMessage() {
			MessageLogin msg = new MessageLogin(Gt06ProtocolConstant.MSG_LOGIN);
			msg.DeviceType = Hardware.getDeviceType();
			msg.Language = Hardware.getLanguage();
			return msg;
		}	
		
		@Override
		public CountDown getCountDown(){
			if(null == mCountdown){
				mCountdown = new CountDown(TIMEOUT, new TimeOutListener() {			
					@Override
					public void onTimeout() {
						if(++mCount > MAXCOUNT){
							Hardware.rebootOnTime();
						}else{
							exec();
						}
					}
				});
			}
			return mCountdown;
		}
		
		@Override
		public EchoListener getEchoListener(){
			if(null == mEchoListener){
				mEchoListener = new EchoListener() {
					@Override
					public void onEcho() {
						if(null != mCountdown) 
							mCountdown.cancel();
					}
				};
			}
			return mEchoListener;
		}
	};
	
	Task TaskPosition = new Task() {

		@Override
		public void exec() {						
			Message msg = getMessage();
			sc.writeAndFlush(msg);
		}

		public Message getMessage() {
			MessagePosition msg = new MessagePosition(Gt06ProtocolConstant.MSG_GPS_LBS_2);
			msg.position = Hardware.getPosition();
			msg.mcc = Hardware.getmcc();
			msg.mnc = Hardware.getmnc();
			msg.lac = Hardware.getlac();
			msg.cellId = Hardware.getcellId();
			return msg;
		}	
		public CountDown getCountDown() {return null;};
		public EchoListener getEchoListener() {return null;};
	};
	
	Task TaskHeartbeat = new Task() {
		int PERIOD = 10*60*1000;
		final int TIMEOUT = 5000;
		int mCount = 0;
		final int MAXCOUNT = 3;
		CountDown mCountdown;
		EchoListener mEchoListener; 
		
		@Override
		public void exec() {	
			
			while(true){
				Message msg = getMessage();
				sc.writeAndFlush(msg);
				mCountdown.start();
				
				try {
					Thread.sleep(PERIOD);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public Message getMessage() {
			MessageHeartbeat msg = new MessageHeartbeat(Gt06ProtocolConstant.MSG_HEARTBEAT);
			msg.electronic_b7=Hardware.isElectronicOn();
			msg.gpsfix_b6=Hardware.isGpsFixed();
			msg.recharge_b2=Hardware.isRecharge();
			msg.acc_b1=Hardware.isAccon();
			msg.guard_b0=Hardware.isInguard();
			msg.valtage=Hardware.geValtage();
			msg.gsmSignal=Hardware.getSignal();
			msg.language=Hardware.getLanguage();
			return msg;
		}			
		
		@Override
		public CountDown getCountDown(){
			if(null == mCountdown){
				mCountdown = new CountDown(TIMEOUT, new TimeOutListener() {			
					@Override
					public void onTimeout() {
						if(++mCount > MAXCOUNT){
							Hardware.rebootOnTime();
						}else{
							exec();
						}
					}
				});
			}
			return mCountdown;
		}
		
		@Override
		public EchoListener getEchoListener(){
			if(null == mEchoListener){
				mEchoListener = new EchoListener() {
					@Override
					public void onEcho() {
						if(null != mCountdown) 
							mCountdown.cancel();
					}
				};
			}
			return mEchoListener;
		}
	};	
}
