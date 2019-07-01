package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.ApnSetting;
import com.yusun.cartracker.api.Hardware;

public class SCXSZ implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SCXSZ;
	}

	@Override
	public void doCmd(SMS msg) {
		/*
		APN:CMNET,1,10.0.0.172,,;
		LBS_GPS_Ephemeris:20,10,30;
		Server:222.111.123.321,54321,TCP;
		*/
		StringBuilder sb = new StringBuilder();
		//APN
		ApnSetting.APN apn = AppContext.instance().getmApnSetting().getCurrentApnInfo();
		sb.append("APN:");
		sb.append(apn.getName());
		sb.append(","+apn.getNetid());
		sb.append(","+apn.getIp());
		if(null != apn.getUser() && !apn.getUser().isEmpty()){
			sb.append("," + apn.getUser());
		}
		if(null != apn.getPass() && !apn.getPass().isEmpty()){
			sb.append("," + apn.getPass());
		}
		sb.append(";");
		
		//timezone
		sb.append(Hardware.instance().getTimeZone2()+";");
		
		//interval
		sb.append("LBS_GPSUpload_GPSWork:");
		sb.append(""+Hardware.instance().getLbsInterval()/60);
		sb.append(","+Hardware.instance().getGpsInterval());
		sb.append(","+Hardware.instance().getGpsWorkInterval()/60);
		sb.append(";");		
		
		//Server
		sb.append("Server:");		
		sb.append(","+Hardware.instance().getIp());
		sb.append(","+Hardware.instance().getPort());
		sb.append(","+Hardware.instance().getProtocol());
		sb.append(";");
		
		//sos
		sb.append(Hardware.instance().getSOS() + ";");
		
		//sensor time
		sb.append(Hardware.instance().getSensor_Time() + ";");
		
		//gps address analyse
		sb.append(Hardware.instance().getGpsAddressAnalyser() + ";");
		
		//alarm time
		sb.append(Hardware.instance().getAlarm_Time() + ";");
		
		//electric
		sb.append(Hardware.instance().isOilPowerControl() ? 1 : 0 + ";");
		
		msg.sendAck(sb.toString());
	}
	
}