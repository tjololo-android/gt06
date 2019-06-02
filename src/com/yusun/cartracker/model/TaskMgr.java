package com.yusun.cartracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.util.Logger;

import android.os.Handler;
import android.os.HandlerThread;

public class TaskMgr{
	Logger logger = new Logger(TaskMgr.class);
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
		logger.info("init");
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
	public void uninit(){
		mHandler.removeCallbacksAndMessages(null);
	}
	public void onEcho(int cmd) {
		logger.info("onEcho cmd="+Integer.toHexString(cmd));
		mHandler.removeMessages(cmd);
		Task t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_SUCESS);
		}
	}	
	void onTimeout(int cmd){
		logger.info("onTimeout="+Integer.toHexString(cmd));
		Task t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_TIMEOUT);
		}
	}

	public void post(Task task){
		logger.info("post cmd="+Integer.toHexString(task.getId()));
		mHandler.post(task);
	}
	public void postDelayed(Task task, long delayMillis){
		logger.info("postDelayed cmd="+Integer.toHexString(task.getId()));
		mHandler.postDelayed(task, delayMillis);
	}
	void sendMessage(Message msg){
		AppContext.instance().getClient().sendMessage(msg);
	}
	void sendEmptyMessageDelayed(int id, long delayMillis){
		mHandler.sendEmptyMessageDelayed(id, delayMillis);
	}
}
