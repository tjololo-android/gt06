package com.yusun.cartracker.model.sms;

import java.text.DecimalFormat;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.Utils;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;
import com.yusun.cartracker.position.Position;

public class WHERE implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.WHERE;
	}

	@Override
	public void doCmd(MSG msg) {
		StringBuilder sb = new StringBuilder();
		if(!Hardware.instance().getGpsFixed()){
			sb.append("No Data!");
		}else{
			Position pos = AppContext.instance().getDatabaseHelper().selectPosition();
			DecimalFormat df=new DecimalFormat("#.000000");
			sb.append("Lat:" + (pos.getLatitude() > 0 ? "N" : "S") + df.format(Math.abs(pos.getLatitude())));
			sb.append(",Lon:" + (pos.getLongitude() < 0 ? "E" : "W") + df.format(Math.abs(pos.getLongitude())));
			sb.append(",Course:" + pos.getCourse());
			sb.append(",Speed:" + pos.getSpeed());
			sb.append(",DateTime:"+ Utils.getDataTime(pos.getTime()));	
		}
		msg.sendAck(sb.toString());
	}	
}
