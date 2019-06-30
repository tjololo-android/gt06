package com.yusun.cartracker.model.sms;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SmsCmdManager {
	Queue<SMS> SmsQueues = new ConcurrentLinkedQueue<SMS>();
	HashMap<String, CmdHandler> mHandler = new HashMap<String, CmdHandler>();

	private void reg(CmdHandler handler) {
		mHandler.put(handler.getCmd().toLowerCase(), handler);
	}

	public void addReq(String num, String msg) {
		SMS sMsg = SMS.fromSms(num, msg);		
		if(null != sMsg){
			synchronized (SmsQueues) {
				SmsQueues.add(sMsg);
				SmsQueues.notify();	
			}			
		}else{
			SMS.sendSms(num, "format error!");
		}
	}

	private void handle(SMS sMsg) {		
		CmdHandler handler = mHandler.get(sMsg.cmd.toLowerCase());
		if (handler != null) {
			handler.doCmd(sMsg);
		} else {
			sMsg.sendAck("unknow command " + sMsg.cmd);
		}
	}

	public void init() {
		reg(new Verson());
		reg(new GPRSSET());
		reg(new ApnHandler());
		reg(new TIMER());
		reg(new SERVER());
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
				SMS sms = SmsQueues.poll();			
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