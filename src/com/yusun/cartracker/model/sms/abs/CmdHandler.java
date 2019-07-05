package com.yusun.cartracker.model.sms.abs;

public interface CmdHandler {
	public String getCmd();
	public void doCmd(MSG msg);

}
