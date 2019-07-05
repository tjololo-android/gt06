package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class PARAM implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.PARAM;
	}

	@Override
	public void doCmd(MSG msg) {
		/*IMEI:353419030080159;
		TimeZone:E8;
		SOS:13312345678,137123456789,137123456789;
		Sensor:10;
		Alarm:30;
		*/
//		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
//		TimeZone.setDefault(tz);
//		String str = TimeZone.getDefault().getDisplayName();
//		final Calendar now = Calendar.getInstance();
//		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
//		now.setTimeZone(tz);
		
		StringBuilder sb = new StringBuilder();
		sb.append("IMEI:"+Hardware.instance().getIMEI()+";");
		sb.append("TimeZone:"+Hardware.instance().getTimeZone2()+";");
		sb.append("SOS:"+Hardware.instance().getSOS()+";");
		sb.append("Sensor:"+Hardware.instance().getSensorInterval()+";");
		sb.append("Alarm:"+Hardware.instance().getDefenseDelay()+";");
		
		msg.sendAck(sb.toString());
	}
	
}
