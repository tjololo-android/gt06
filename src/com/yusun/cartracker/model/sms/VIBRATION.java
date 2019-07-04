package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class VIBRATION implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.VIBRATION;
	}

	@Override
	public void doCmd(SMS msg) {
		Matcher m = Pattern.compile("(ON|OFF)").matcher(msg.content);
		if(m.find()){
			Hardware.instance().setVibration("ON".equals(m.group(1)));
		}else{
			msg.sendFormatErr();
		}
	}	
}
