package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.ApnSetting;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class APNN implements CmdHandler{
	String reg = "(\\w+),*(\\w+)*,*(\\d+)*,*(\\d+)*,*(\\d+\\.\\d+\\.\\d+\\.\\d+)*";
	
	@Override
	public String getCmd() {
		return CMDS.APN;
	}

	@Override
	public void doCmd(SMS msg) {		
		//APN,666666,CMNET,admin,123456,0,0.0.0.0#		
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(msg.content);
		if(m.find()){
			ApnSetting apnSet = AppContext.instance().getmApnSetting();
			ApnSetting.APN apn = apnSet.newApn();
			
			apn.setName(m.group(1));
			apn.setUser(m.group(2));
			apn.setPass(m.group(3));
			apn.setNetid(m.group(4));
			apn.setIp(m.group(5));		
			
			if(apnSet.update(apn)){
				msg.sendAck("OK");
			}else{			
				msg.sendAck("ERROR");
			}
		}else{
			msg.sendFormatErr();
		}
	}	
}
