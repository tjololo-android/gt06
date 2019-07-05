package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class SENDS implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SENDS;
	}

	@Override
	public void doCmd(MSG msg) {
		Matcher m = Pattern.compile("(\\d+)").matcher(msg.content);
		if(m.find()){
			Hardware.instance().setSendsTimeout(Integer.parseInt(m.group(1))*60);
		}else{
			msg.sendFormatErr();
		}
	}	
}
