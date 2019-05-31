package com.yusun.cartracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.provider.CalendarContract.Instances;

public class EchoMgr{
	private final Map<Integer, EchoListener> mListeners = new ConcurrentHashMap<Integer, EchoListener>();
	public synchronized void onEcho(int cmd){
		EchoListener listener = mListeners.get(cmd);
		if(null != listener){
			listener.onEcho();
		}
	}

	
	public void setListener(int cmd, EchoListener listener){
		mListeners.put(cmd, listener);
	}
	
	public interface EchoListener{
		void onEcho();
	}
	
	/**
	 * single
	 */
	private EchoMgr(){};
	private static EchoMgr _this = new EchoMgr();
	public static EchoMgr instance(){
		return _this;
	}
}
