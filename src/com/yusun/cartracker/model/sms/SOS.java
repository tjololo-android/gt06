package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class SOS implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SOS;
	}

	@Override
	public void doCmd(SMS msg) {
		
	}	
}
