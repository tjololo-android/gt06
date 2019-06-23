package com.yusun.cartracker.model.sms;

public interface CmdHandler {
	public String getCmd();
	public void doCmd(SMS msg);

}
