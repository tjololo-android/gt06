package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;

public class SERVER implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.SERVER;
	}

	@Override
	public void doCmd(SMS msg) {
		//SERVER,666666,0,202.173.231.112,8821,0#
		String[] pm = msg.content.split(",");
		String ip = pm[3];
		String port = pm[4];					
		if(Hardware.instance().setService(ip, port)){
			msg.sendAck("OK");
			System.exit(1);	//reboot app for service changed
		}else{
			msg.sendAck("ERROR");
		}			
	}
}
