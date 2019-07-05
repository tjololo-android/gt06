package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class STATUS implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.STATUS;
	}

	@Override
	public void doCmd(MSG msg) {
		StringBuilder sb = new StringBuilder();
		//battery
		int percent = Hardware.instance().getBATTERY();
		sb.append("BATTERY:");
		if(percent < 10){
			sb.append("Battery too low!Warning!");
		}else if(percent < 20){
			sb.append("LOW STATUS");
		}else{
			sb.append("NORMAL");
		}
		sb.append(";");
		
		//gps
		sb.append("GPS:");
		if(!Settings.instance().isGpsPowerOn()){
			sb.append("GPS OFF");
		}else if(Hardware.instance().getGpsFixed()){
			sb.append("Successful Positioning");
		}else{
			sb.append("Searching Satellite");
		}
		sb.append(";");
		
		//gprs
		sb.append("GPRS:");
		if(AppContext.instance().isNetworkConnected()){
			sb.append("Link UP");
		}else{
			sb.append("Link Down");
		}
		sb.append(";");
		
		//OilPowerControl
		sb.append("OilPowerControl:");
		sb.append(Hardware.instance().getOilPowerControl() ? "ON" : "OFF");
		sb.append(";");
		
		msg.sendAck(sb.toString());
	}
	
}
