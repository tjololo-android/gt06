package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class SERVER implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.SERVER;
	}

	@Override
	public void doCmd(SMS msg) {
		//SERVER,666666,0,202.173.231.112,8821,0#
		String[] pm = msg.content.split(",");
		if(pm.length < 3){
			msg.sendAck("ERROR");
			return;
		}		
		String ip = pm[1].trim();
		String port = pm[2].trim();		
		if(Hardware.instance().setService(ip, port)){
			AppContext.instance().resetNetServer();
			msg.sendAck("OK");			
		}else{
			msg.sendAck("ERROR");
		}			
	}
}
