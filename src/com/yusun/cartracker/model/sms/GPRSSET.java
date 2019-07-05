package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.ApnSetting;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class GPRSSET implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.GPRSSET;
	}

	@Override
	public void doCmd(MSG msg) {
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
		
		//interval
		sb.append("LBS_GPSUpload_GPSWork:");
		sb.append(""+Settings.instance().getLbsInterval()/60);
		sb.append(","+Settings.instance().getGpsInterval());
		sb.append(","+Settings.instance().getGpsWorkInterval()/60);
		sb.append(";");
		
		//Server
		sb.append("Server:");		
		sb.append(","+Settings.instance().getIp());
		sb.append(","+Settings.instance().getPort());
		sb.append(","+Settings.instance().getProtocol());
		sb.append(";");
		
		msg.sendAck(sb.toString());
	}
	
}
