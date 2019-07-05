package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.Fence;
import com.yusun.cartracker.model.FenceCircle;
import com.yusun.cartracker.model.FenceRectangle;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class SEEFENCE implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SEEFENCE;
	}
	@Override
	public void doCmd(MSG msg) {
		//State:OUT;Lat:N23.116615;Lon:E114.416000;Radius:20000M;FenceType:Circle;
		StringBuilder sb = new StringBuilder();
		Fence fence = Hardware.instance().getFence();
		sb.append("State:"+fence.getType()+";");
		sb.append("State:"+fence.getType()+";");
		if(Fence.FENCE_SHARP_CIRCLE == fence.getSharp()){
			FenceCircle fc = (FenceCircle)fence;
			sb.append("Lat:"+get(fc.getLat(), true));
			sb.append("Lon:"+get(fc.getLon(), false));
			sb.append("Radio:"+fc.getRadius());
			sb.append("FenceType:"+ "Circle" + ";");
		}else{
			FenceRectangle fc = (FenceRectangle)fence;
			sb.append("Lat1:"+get(fc.getLeftTopLan(), true));
			sb.append("Lon1:"+get(fc.getLeftTopLon(), false));
			sb.append("Lat2:"+get(fc.getRightBottomLan(), true));
			sb.append("Lon2:"+get(fc.getRightBottomLon(), false));
			sb.append("FenceType:"+ "Rectangle" + ";");
		}
		msg.sendAck(sb.toString());
	}	
	String get(double val, boolean isLan){
		String flag;
		if(val > 0 && isLan) flag = "N";
		else if(val < 0 && isLan) flag = "S";
		else if(val > 0 && !isLan) flag = "E";
		else flag = "W";
		return flag+Math.abs(val);
	}
}
