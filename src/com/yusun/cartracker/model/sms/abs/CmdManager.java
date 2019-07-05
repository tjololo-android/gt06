package com.yusun.cartracker.model.sms.abs;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.yusun.cartracker.model.sms.APNN;
import com.yusun.cartracker.model.sms.DEFENSE;
import com.yusun.cartracker.model.sms.DW;
import com.yusun.cartracker.model.sms.EURL;
import com.yusun.cartracker.model.sms.FACTORY;
import com.yusun.cartracker.model.sms.FENCE;
import com.yusun.cartracker.model.sms.GMT;
import com.yusun.cartracker.model.sms.GPRSSET;
import com.yusun.cartracker.model.sms.MONITOR;
import com.yusun.cartracker.model.sms.PARAM;
import com.yusun.cartracker.model.sms.PASSWORD;
import com.yusun.cartracker.model.sms.RECOVER;
import com.yusun.cartracker.model.sms.RELAY;
import com.yusun.cartracker.model.sms.RESET;
import com.yusun.cartracker.model.sms.SCXSZ;
import com.yusun.cartracker.model.sms.SEEFENCE;
import com.yusun.cartracker.model.sms.SENDS;
import com.yusun.cartracker.model.sms.SENSOR;
import com.yusun.cartracker.model.sms.SERVER;
import com.yusun.cartracker.model.sms.SETGPS;
import com.yusun.cartracker.model.sms.SOS;
import com.yusun.cartracker.model.sms.STATUS;
import com.yusun.cartracker.model.sms.TIMER;
import com.yusun.cartracker.model.sms.URL;
import com.yusun.cartracker.model.sms.VIBRATION;
import com.yusun.cartracker.model.sms.Verson;
import com.yusun.cartracker.model.sms.WHERE;

public class CmdManager {
	Queue<MSG> SmsQueues = new ConcurrentLinkedQueue<MSG>();
	HashMap<String, CmdHandler> mHandler = new HashMap<String, CmdHandler>();

	private void reg(CmdHandler handler) {
		mHandler.put(handler.getCmd().toLowerCase(), handler);
	}

	public void addReq(String num, String msg) {
		MSG sMsg = SMS.fromMsg(num, msg);		
		if(null != sMsg){
			synchronized (SmsQueues) {
				SmsQueues.add(sMsg);
				SmsQueues.notifyAll();	
			}			
		}else{
			SMS.sendMsg(num, "format error!");
		}
	}
	
	public void addGprsReq(String msg, int serverFlag) {
		GPRS sMsg = GPRS.fromMsg(msg, serverFlag);
		if(null != sMsg){
			synchronized (SmsQueues) {
				SmsQueues.add(sMsg);
				SmsQueues.notifyAll();	
			}		
		}else{
			GPRS.sendMsg("format error!", serverFlag);
		}
	}

	private void handle(MSG msg) {		
		CmdHandler handler = mHandler.get(msg.cmd.toLowerCase());
		if (handler != null) {
			handler.doCmd(msg);
		} else {
			msg.sendAck("unknow command " + msg.cmd);
		}
	}

	public void init() {
		reg(new Verson());
		reg(new GPRSSET());
		reg(new APNN());
		reg(new TIMER());
		reg(new SERVER());
		reg(new PARAM());
		reg(new SCXSZ());
		reg(new STATUS());
		reg(new WHERE());
		reg(new DW());
		reg(new URL());
		reg(new EURL());
		reg(new FENCE());
		reg(new SEEFENCE());
		reg(new SETGPS());
		reg(new PASSWORD());
		reg(new RECOVER());
		reg(new RESET());
		reg(new FACTORY());
		reg(new GMT());
		reg(new SOS());
		reg(new SENSOR());
		reg(new SENDS());
		reg(new DEFENSE());
		reg(new RELAY());
		reg(new MONITOR());
		reg(new VIBRATION());
		start();
	}
	private void start(){
		new Thread(){
			public void run(){
				loop();
			}
		}.start();
	}
	private volatile boolean looping = false;
	private void loop(){
		looping = true;
		while(looping){		
			if(SmsQueues.isEmpty()){
				try {
					synchronized (SmsQueues){
						SmsQueues.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(SmsQueues.size() > 0){
				MSG sms = SmsQueues.poll();			
				handle(sms);			
			}
		}
	}

	public void uninit() {
		looping = false;
		mHandler.clear();
		SmsQueues.clear();
	}	
}