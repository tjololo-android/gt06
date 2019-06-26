package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.APN;
import com.yusun.cartracker.api.Hardware;

public class TIMER implements CmdHandler{

	@Override
	public String getCmd() {
		// TODO Auto-generated method stub
		return CMDS.TIMER;
	}

	@Override
	public void doCmd(SMS msg) {
		//TIMER,666666, 2,10,30#
		try{
			String[] pm = msg.content.split(",");
			String s = pm[2];
			if(null != s && !s.isEmpty()){
				int lbs = Integer.parseInt(s);
				Hardware.instance().setLbsInterval(lbs * 60);
			}
			s = pm[3];
			if(null != s && !s.isEmpty()){
				int gps = Integer.parseInt(s);
				Hardware.instance().setLbsInterval(gps);
			}
			s = pm[4];
			if(null != s && !s.isEmpty()){
				int gpsWork = Integer.parseInt(s);
				Hardware.instance().setLbsInterval(gpsWork * 60);
			}
			msg.sendAck("OK");
		} catch (NumberFormatException e){
			e.printStackTrace();
			msg.sendAck("ERROR");
		}
	}	
}
