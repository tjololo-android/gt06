package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;

public class TIMER implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.TIMER;
	}

	@Override
	public void doCmd(SMS msg) {
		//TIMER,666666, 2,10,30#
		try{
			String[] pm = msg.content.split(",");
			if(pm.length > 2){
				int lbs = Integer.parseInt(pm[0].trim());
				Hardware.instance().setLbsInterval(lbs * 60);
				
				int gps = Integer.parseInt(pm[1].trim());
				Hardware.instance().setGpsInterval(gps);
				
				int gpsWork = Integer.parseInt(pm[2].trim());
				Hardware.instance().setGpsWorkInterval(gpsWork * 60);
				msg.sendAck("OK");
				return;
			}
		} catch (NumberFormatException e){
			e.printStackTrace();			
		} catch (NullPointerException e){
			e.printStackTrace();			
		}
		msg.sendAck("ERROR");
	}	
}
