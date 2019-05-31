package com.yusun.cartracker.model;

public abstract class Command extends Object{
	int cmd;
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	abstract public void exec(Object content);
}
