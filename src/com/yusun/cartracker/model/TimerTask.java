package com.yusun.cartracker.model;

import com.yusun.cartracker.AppContext;

public abstract class TimerTask extends Task{
	public TimerTask(int id) {
		super(id);
	}
	private int TIME_OUT = -1;
	public void setTimeOut(int timeout){
		TIME_OUT = timeout;
	}		
	public void run() {
		TaskMgr tm = AppContext.instance().getmTaskMgr(); 
		Message msg = getMessage();		
		tm.sendMessage(msg);
		if(-1 != TIME_OUT){
			tm.sendEmptyMessageDelayed(msg.id, TIME_OUT);
		}
	}		
	public abstract Message getMessage();
}