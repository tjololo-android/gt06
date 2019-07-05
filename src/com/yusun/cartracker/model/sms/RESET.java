package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class RESET implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.RESET;
	}

	@Override
	public void doCmd(MSG msg) {		
		msg.sendAck("The terminal will restart after 1 minute!");
	}	
}
