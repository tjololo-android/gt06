package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class SETGPS implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SETGPS;
	}
	
	@Override
	public void doCmd(MSG msg) {
		if(!"ON".equals(msg.content) && !"OFF".equals(msg.content)){		
			msg.sendFormatErr();
			return;
		}
		
		boolean on = true;
		if("OFF".equals(msg.content))
			on = false;
		
		if(Settings.instance().turnOnGps(on)){
			msg.sendAck("OK");
		}else{
			msg.sendAck("ERROR");
		}
	}	
}
