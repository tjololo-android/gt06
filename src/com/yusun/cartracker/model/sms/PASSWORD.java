package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class PASSWORD implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.PASSWORD;
	}

	@Override
	public void doCmd(SMS msg) {
		Matcher m = Pattern.compile("(\\d+),(\\d+)").matcher(msg.content);
		if(m.find()){
			if(Hardware.instance().checkPass(m.group(1))){
				Hardware.instance().modifyPass(m.group(2));
			}else{
				msg.sendPassErr();
			}
		}else{
			msg.sendFormatErr();
		}
	}	
}
