package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class TIMER implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.TIMER;
	}

	@Override
	public void doCmd(MSG msg) {
		//TIMER,666666, 2,10,30#
		String reg = "(\\d+)*,(\\d+)*,(\\d+)*";
		Matcher m = Pattern.compile(reg).matcher(msg.content);
		if(m.find()){
			String s = m.group(1);
			if(null != s && !s.isEmpty()){
				int lbs = Integer.parseInt(s);
				Hardware.instance().setLbsInterval(lbs * 60);
			}
			s = m.group(2);
			if(null != s && !s.isEmpty()){
				int gps = Integer.parseInt(s);
				Hardware.instance().setGpsInterval(gps);
			}			
			s = m.group(3);
			if(null != s && !s.isEmpty()){
				int gpsWork = Integer.parseInt(s);
				Hardware.instance().setGpsWorkInterval(gpsWork * 60);
			}
			msg.sendAck("OK");			
		}else{	
			msg.sendFormatErr();
		}
	}	
}
