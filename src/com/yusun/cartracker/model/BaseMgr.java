package com.yusun.cartracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;
import android.os.HandlerThread;
import android.provider.CalendarContract.Instances;

public abstract class BaseMgr implements IMgr{
	private final Map<Integer, Task> mTasks = new ConcurrentHashMap<Integer, Task>();	
	public void reg(Task cmd){
		mTasks.put(cmd.getId(), cmd);
	}
	public Task get(int cmd){
		return mTasks.get(cmd);
	}
	@Override
	public void uninit() {
		mTasks.clear();
	}
}
