package com.yusun.cartracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yusun.cartracker.CarContext;

import android.os.Handler;
import android.os.HandlerThread;

public class TaskMgr{
	Handler mHandler;
	public static final int RESULT_SUCESS = 0;
	public static final int RESULT_TIMEOUT = 1;
	private final Map<Integer, Task> mTasks = new ConcurrentHashMap<Integer, Task>();	
	public void reg(Task task){
		mTasks.put(task.getId(), task);
	}
	public Task get(int cmd){
		return mTasks.get(cmd);
	}	
	public void init(){
		HandlerThread ht = new HandlerThread("worker");
		ht.start();
		mHandler = new Handler(ht.getLooper()){
			@Override
			public void handleMessage(android.os.Message msg) {
				onTimeout(msg.what);
				super.handleMessage(msg);
			}
		};
	}	
	public void onEcho(int cmd) {
		mHandler.removeMessages(cmd);
		Task t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_SUCESS);
		}
	}	
	void onTimeout(int cmd){
		Task t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_TIMEOUT);
		}
	}

	public void sendMessage(Task task){
		mHandler.post(task);
	}
	void sendMessage(Message msg){
		CarContext.instance().getClient().sendMessage(msg);
	}
	void sendEmptyMessageDelayed(int id, int timeout){
		
	}
}
