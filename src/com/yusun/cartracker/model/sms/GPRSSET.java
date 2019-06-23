package com.yusun.cartracker.model.sms;

public class GPRSSET implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.GPRSSET;
	}

	@Override
	public void doCmd(SMS msg) {
		/*
		APN:CMNET,1,10.0.0.172,,;
		LBS_GPS_Ephemeris:20,10,30;
		Server:222.111.123.321,54321,TCP;
		*/
		
	}
	
}
