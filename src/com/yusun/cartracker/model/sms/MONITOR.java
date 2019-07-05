package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;
import com.yusun.cartracker.model.sms.abs.MSG;

public class MONITOR implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.MONITOR;
	}

	@Override
	public void doCmd(MSG sms) {
		SMS msg = (SMS) sms;
		if(Hardware.instance().getSOS().contains(msg.getPhoneNum())){
			Hardware.instance().switchMonitor();
			msg.sendOK();
		}else{
			msg.sendAck("not a sos phonenumber");;
		}
	}	
}
