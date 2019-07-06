package com.yusun.cartracker.model.sms;

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
			sb.append("Lat:" + Utils.getLanString(pos.getLatitude()));
			sb.append(",Lon:" + Utils.getLonString(pos.getLongitude()));
			sb.append(",Course:" + pos.getCourse());
			sb.append(",Speed:" + pos.getSpeed());
			sb.append(",DateTime:"+ Utils.getDataTime(pos.getTime()));	
		}
		msg.sendAck(sb.toString());
	}	
}
