package com.yusun.cartracker.protocol;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.Fence;
import com.yusun.cartracker.position.Position;

import android.content.BroadcastReceiver;

public class AlarmMgr {
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
			checkSos();
			checkPower();
			checkVibration();			
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
	
	void checkSos(){
		
	}
	void checkPower(){
		if(Hardware.instance().getBATTERY() < 10){
			alarm();
		}
	}
	void checkVibration(){
		
	}
	void checkFence(){		//NG
		Fence fence = Settings.instance().getFence();
		Position pos = Hardware.instance().getLastPos();
		if(fence.inSharp(pos.getLatitude(), pos.getLongitude())){
			alarm();
		}
	}
	BroadcastReceiver receiver = new BroadcastReceiver(){ //NG
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			if(intent.getAction().equals("sos_key")){
				//sos
				raseEvent();
			}
		};
	};
	public void init() {
		start();		
	}
	public void uninit() {
		stop();
	}
}
