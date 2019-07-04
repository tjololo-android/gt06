package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class RESET implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.RESET;
	}

	@Override
	public void doCmd(SMS msg) {		
		msg.sendAck("The terminal will restart after 1 minute!");
		Hardware.instance().rebootAfter1Minute();
	}	
}
