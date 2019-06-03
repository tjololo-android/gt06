package com.yusun.cartracker.model;

import android.os.Handler;
import android.os.HandlerThread;

public class CmdMgr extends BaseMgr{
	Handler mHandler;
	@Override
	public void init() {
		HandlerThread ht = new HandlerThread("cmdMgr");
		mHandler = new Handler(ht.getLooper());
	}

	@Override
	public void uninit() {
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
