package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class SERVER implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.SERVER;
	}

	@Override
	public void doCmd(MSG msg) {
		//SERVER,666666,0,202.173.231.112,8821,0#
		String reg = "([0-1]),(.*),(\\d+),([0-1])";
		Matcher m = Pattern.compile(reg).matcher(msg.content);
		if(m.find()){			
			if(Hardware.instance().setService(m.group(2), m.group(3))){
				AppContext.instance().resetNetServer();
			}else{
				msg.sendErr();
			}
		}else{
			msg.sendFormatErr();
		}
	}
}
