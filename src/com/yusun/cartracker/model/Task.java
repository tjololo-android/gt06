package com.yusun.cartracker.model;

import com.yusun.cartracker.model.EchoMgr.EchoListener;

public abstract class Task extends Object{
	abstract public void exec();	
	abstract public EchoListener getEchoListener();
	abstract public CountDown getCountDown();
}
