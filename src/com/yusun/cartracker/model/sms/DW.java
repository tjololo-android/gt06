package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;
import com.yusun.cartracker.protocol.Gt06ProtocolConstant;

public class DW implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.DW;
	}

	@Override
	public void doCmd(MSG msg) {
		AppContext.instance().getProtocol().doCmd(Gt06ProtocolConstant.CMD_REQUEST_ADDRESS, msg);	
	}	
}
