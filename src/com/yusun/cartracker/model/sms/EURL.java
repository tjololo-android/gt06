package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class EURL implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.EURL;
	}

	@Override
	public void doCmd(MSG msg) {		
		if(Settings.instance().setGPSAnalyseUrl(msg.content)){
			msg.sendOK();
		}else{
			msg.sendAck("ERROR");
		}
	}	
}
