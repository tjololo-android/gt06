package com.yusun.cartracker.model.sms;

public class Verson implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.VERSION;
	}

	@Override
	public void doCmd(SMS msg) {
		
	}
	
}
