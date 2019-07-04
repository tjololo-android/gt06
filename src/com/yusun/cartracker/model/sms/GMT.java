package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class GMT implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.GMT;
	}

	@Override
	public void doCmd(SMS msg) {
		//GMT,666666,E,8#
		String reg = "(E|W),([0-9]|1[0-2])";
		Matcher m = Pattern.compile(reg).matcher(msg.content);
		if(m.find()){
			Hardware.instance().setTimeZone(m.group(1), m.group(2));
		}else{
			msg.sendFormatErr();
		}
	}	
}
