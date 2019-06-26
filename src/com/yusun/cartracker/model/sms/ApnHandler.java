package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.APN;
import com.yusun.cartracker.api.ApnSetting;

public class ApnHandler implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.APN;
	}

	@Override
	public void doCmd(SMS msg) {	
		//APN,666666, CMNET,admin,123456,0#
		String[] pm = msg.content.split(",");
		APN apn = new APN();
		try{
			apn.setName(pm[2]);
			apn.setUser(pm[3]);
			apn.setPass(pm[4]);
			apn.setNetid(pm[5]);
			apn.setIp(pm[6]);	
		}catch(IndexOutOfBoundsException e){			
		}
		if(AppContext.instance().getmApnSetting().update(apn)){
			msg.sendAck("OK");
		}else{			
			msg.sendAck("ERROR");
		}
	}
	
}
