package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class EURL implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.EURL;
	}

	@Override
	public void doCmd(SMS msg) {		
		if(Hardware.instance().setGPSAnalyseUrl(msg.content)){
			msg.sendAck("OK");
		}else{
			msg.sendAck("ERROR");
		}
	}	
}
