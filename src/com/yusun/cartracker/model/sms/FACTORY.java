package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class FACTORY implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.FACTORY;
	}

	@Override
	public void doCmd(MSG msg) {
		Hardware.instance().factory();
		msg.sendOK();
	}	
}
