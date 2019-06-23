package com.yusun.cartracker.model.sms;

public class Versin implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.VERSION;
	}

	@Override
	public void doCmd(SMS msg) {
		// TODO Auto-generated method stub
		
	}
	
}
