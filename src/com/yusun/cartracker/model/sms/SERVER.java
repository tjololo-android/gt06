package com.yusun.cartracker.model.sms;

public class SERVER implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.SERVER;
	}

	@Override
	public void doCmd(SMS msg) {
		//SERVER,666666,0,202.173.231.112,8821,0#
		String[] pm = msg.command.split(",");
		String ip;
		String port;
		try{			
			ip = pm[3];
			port = pm[4];
			
		}catch(IndexOutOfBoundsException e){			
		}	
	}
}
