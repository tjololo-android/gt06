package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class PASSWORD implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.PASSWORD;
	}

	@Override
	public void doCmd(MSG msg) {
		Matcher m = Pattern.compile("(\\d{6})").matcher(msg.content);
		if(m.find()){			
			Settings.instance().modifyPass(m.group(1));
			msg.sendOK();			
		}else{
			msg.sendFormatErr();
		}
	}	
}
