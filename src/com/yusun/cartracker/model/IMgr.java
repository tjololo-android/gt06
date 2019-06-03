package com.yusun.cartracker.model;

public interface IMgr{
	void reg(Task t);
	public Task get(int cmd);
	void init();
	void uninit();
	void start();
	void stop();
}
