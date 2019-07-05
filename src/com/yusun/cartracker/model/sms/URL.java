package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class URL implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.URL;
	}

	@Override
	public void doCmd(MSG msg) {
		msg.sendAck(Settings.instance().getGPSAnalyseUrl());
	}	
}
