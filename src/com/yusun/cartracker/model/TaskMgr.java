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
	HandlerThread mHandlerThread;
	public static final int RESULT_SUCESS = 0;
	public static final int RESULT_TIMEOUT = 1;
	private final Map<Integer, TimerTask> mTasks = new ConcurrentHashMap<Integer, TimerTask>();	
	public void reg(TimerTask t) {
		mTasks.put(t.getId(), t);		
	}
	public TimerTask get(int cmd){
		return mTasks.get(cmd);
	}	
	public void init(){
		logger.info("init");
		mHandlerThread = new HandlerThread("taskMgr");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()){
			@Override
			public void handleMessage(android.os.Message msg) {
				onTimeout(msg.what);
				super.handleMessage(msg);
			}
		};
	}	
	public void uninit(){	
		mHandlerThread.quit();
	}
	public void start(){}
	public void stop(){
		mHandler.removeCallbacksAndMessages(null);
	}
	public void unreg(){
		mTasks.clear();
	}
	public void onEcho(int cmd) {
		logger.info("onEcho cmd="+Integer.toHexString(cmd));
		mHandler.removeMessages(cmd);
		TimerTask t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_SUCESS);
		}
	}	
	void onTimeout(int cmd){
		logger.info("onTimeout="+Integer.toHexString(cmd));
		TimerTask t = get(cmd);
		if(null != t){
			t.onComplete(RESULT_TIMEOUT);
		}
	}

	public void post(TimerTask task){
		logger.info("post cmd="+Integer.toHexString(task.getId()));
		mHandler.post(task);
	}
	public void postDelayed(TimerTask task, long delayMillis){
		logger.info("postDelayed cmd="+Integer.toHexString(task.getId()));
		mHandler.postDelayed(task, delayMillis);
	}	
	public void sendEmptyMessageDelayed(int id, long delayMillis){
		mHandler.sendEmptyMessageDelayed(id, delayMillis);
	}
	public void sendEmptyMessage(int id){
		mHandler.sendEmptyMessage(id);
	}
	
	public void sendMessage(Message msg){
		AppContext.instance().getClient().sendMessage(msg);
	}
}
