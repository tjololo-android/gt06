package com.yusun.cartracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.provider.CalendarContract.Instances;

public class CmdMgr{
	private final Map<Integer, Command> mCammonds = new ConcurrentHashMap<Integer, Command>();	
	public void reg(Command cmd){
		mCammonds.put(cmd.getCmd(), cmd);
	}
	public Command get(int cmd){
		return mCammonds.get(cmd);
	}
}
