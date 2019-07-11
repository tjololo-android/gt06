package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class RELAY implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.RELAY;
	}

	@Override
	public void doCmd(MSG msg) {
		Matcher m = Pattern.compile("([0-1])").matcher(msg.content);
		if(m.find()){
			Hardware.instance().setOilPowerControl("1".equals(m.group(1)));
			msg.sendOK();
		}else{
			msg.sendFormatErr();
		}
	}	
}
