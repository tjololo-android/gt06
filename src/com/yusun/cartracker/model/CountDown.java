package com.yusun.cartracker.model;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;



public class CountDown extends Thread{
	int timeout;
	TimeOutListener listener;
	Semaphore event = new Semaphore(0);
	
	public CountDown(int timeout, TimeOutListener listener){
		super();
	}

	public void cancel(){
		event.release();
	}	
	public interface TimeOutListener{
		void onTimeout();
	}
	@Override
	public void run() {
		long end = System.currentTimeMillis()+timeout;
		while(System.currentTimeMillis() < end){
			try {
				if(event.tryAcquire(200, TimeUnit.MILLISECONDS)){
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		listener.onTimeout();
		super.run();
	}
}
