package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.ApnSetting;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class ApnHandler implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.APN;
	}

	@Override
	public void doCmd(SMS msg) {	
		//APN,666666, CMNET,admin,123456,0#
		String[] pm = msg.content.split(",");
		ApnSetting apnSet = AppContext.instance().getmApnSetting();
		ApnSetting.APN apn = apnSet.newApn();
		try{
			apn.setName(pm[0].trim());
			apn.setUser(pm[1].trim());
			apn.setPass(pm[2].trim());
			apn.setNetid(pm[3].trim());
			apn.setIp(pm[4].trim());	
		} catch (IndexOutOfBoundsException e){			
		} catch (NullPointerException e){			
		}
		
		if(apnSet.update(apn)){
			msg.sendAck("OK");
		}else{			
			msg.sendAck("ERROR");
		}
	}
	
}
