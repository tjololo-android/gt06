package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class SENSOR implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SENSOR;
	}

	@Override
	public void doCmd(MSG msg) {
		Matcher m = Pattern.compile("(\\d+)").matcher(msg.content);
		if(m.find()){
			Hardware.instance().setSensorInterval(Integer.parseInt(m.group(1)));
		}else{
			msg.sendFormatErr();
		}
	}	
}
