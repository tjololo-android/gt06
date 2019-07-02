package com.yusun.cartracker.model;

public abstract class CommandFactory{
	public abstract Command newCommand(int cmd);
}