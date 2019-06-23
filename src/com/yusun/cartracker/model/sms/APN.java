package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.ApnSetter;

public class APN implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.APN;
	}

	@Override
	public void doCmd(SMS msg) {		
		String[] pm = msg.command.split(",");
		ApnSetter setter = new ApnSetter();
		try{
			setter.setName(pm[2]);
			setter.setUser(pm[3]);
			setter.setPass(pm[4]);
			setter.setNetid(pm[5]);
			setter.setIp(pm[6]);	
		}catch(IndexOutOfBoundsException e){			
		}
		if(setter.update()){
			msg.sendAck("OK");
		}else{			
			msg.sendAck(0, "ERROR");
		}
	}
	
}
