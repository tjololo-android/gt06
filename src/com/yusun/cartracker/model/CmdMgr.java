package com.yusun.cartracker.model;

import android.os.Handler;
import android.os.HandlerThread;

public class CmdMgr extends BaseMgr{
	Handler mHandler;
	HandlerThread handleThread;
	@Override
	public void init() {
		handleThread = new HandlerThread("cmdMgr");
		handleThread.start();
		mHandler = new Handler(handleThread.getLooper());
	}

	@Override
	public void uninit() {
		handleThread.quit();
		super.uninit();
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		mHandler.removeCallbacksAndMessages(null);
	}
	
	public void post(Task t){
		mHandler.post(t);
	}
}
