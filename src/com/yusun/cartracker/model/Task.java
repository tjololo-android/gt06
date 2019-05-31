package com.yusun.cartracker.model;

import com.yusun.cartracker.CarContext;

public abstract class Task implements Runnable{
	private int TIME_OUT = -1;
	private int ID;
	public Task(int id){
		ID = id;
	}
	public int getId(){
		return ID;
	}
	public void setTimeOut(int timeout){
		TIME_OUT = timeout;
	}		
	@Override
	public void run() {
		TaskMgr tm = CarContext.instance().getmTaskMgr(); 
		Message msg = getMessage();
		tm.sendMessage(msg);
		if(-1 != TIME_OUT){
			tm.sendEmptyMessageDelayed(msg.id, TIME_OUT);
		}
	}		
	public abstract void onComplete(int result);
	public abstract Message getMessage();
}