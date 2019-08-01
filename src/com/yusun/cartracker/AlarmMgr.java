package com.yusun.cartracker;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.Fence;
import com.yusun.cartracker.position.Position;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class AlarmMgr {
	public static final String ACTION_ALARM = "yusun.intent.action.ACTION_ALARM";
	public static final String ALARM_TYPE = "ALARM_TYPE"; 
    public static final int ALARM_TYPE_NORMAL = 0; //正常
    public static final int ALARM_TYPE_VIBRATION = 1; //震动
    public static final int ALARM_TYPE_OIL_POWER_OFF = 2; //断电
    public static final int ALARM_TYPE_BATTERY_LOW = 3; //低电
    public static final int ALARM_TYPE_SOS = 4; //SOS	
	
	boolean running = true;
	Semaphore event = new Semaphore(0);
	public void start(){
		new Thread(){
			public void run() {
				waitForEven();
			}
		}.start();
	}
	public void stop(){
		running = false;
		raseEvent();		
	}
	private void waitForEven(){	
		while(running){
			waitEvent(3000);
			checkPower();
			if(0 != Hardware.instance().getAlarmType()){
				alarm();
				Hardware.instance().resetAlarmType();
			}
		}
	}
	
	private void alarm(){
		AppContext.instance().getProtocol().alarm();
	}
	private void waitEvent(int timeout){
		try {
			event.tryAcquire(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void raseEvent(){
		event.release();
	}
	
	void checkPower(){
		if(Hardware.instance().getBATTERY() < 10){
			Hardware.instance().setAlarmType(ALARM_TYPE_BATTERY_LOW);		
			raseEvent();
		}
	}
	void checkFence(){		//NG
		Fence fence = Settings.instance().getFence();
		Position pos = Hardware.instance().getLastPos();
		if(fence.inSharp(pos.getLatitude(), pos.getLongitude())){
			alarm();
		}
	}
	BroadcastReceiver receiver = new BroadcastReceiver(){
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			if(intent.getAction().equals(ACTION_ALARM)){
				int type = intent.getIntExtra(ALARM_TYPE, -1);
				if(-1 != type){
					Hardware.instance().setAlarmType(type);
					raseEvent();
				}
			}
		};
	};
	public void init() {
		Context ctx = AppContext.instance().getContext();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_ALARM);
		ctx.registerReceiver(receiver, filter);
		start();		
	}
	public void uninit() {
		AppContext.instance().getContext().unregisterReceiver(receiver);
		stop();
	}
}
