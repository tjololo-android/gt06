package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.Fence;
import com.yusun.cartracker.model.FenceCircle;
import com.yusun.cartracker.model.FenceRectangle;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.MSG;

public class FENCE implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.FENCE;
	}
	
	double get(String flag, String val){
		double ret = Double.parseDouble(val); 
		if("S".equals(flag) || "W".equals(flag)){
			ret *= -1;
		}
		return ret;
	}

	@Override
	public void doCmd(MSG msg) {				
//		ON,1,N23,W114,N24,E114
//		ON,1,N23,W114,N24,E114,OUT
//		ON,0,N23,W114,10
//		ON,0,N23,W114,10,IN
//		ON,0,0,0,10
		Fence fence = getFence(msg);
		if(null != fence){
			msg.sendAck("OK");
			Hardware.instance().setFence(fence);
		}else{
			msg.sendFormatErr();
		}
	}	
	Fence getFence(MSG msg){
		Fence fence = null;
		String reg1 = "(ON|OFF),1,([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),*(IN|OUT)*";
		String reg2 = "(ON|OFF),0,([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),(\\d+),*(IN|OUT)*";
		String reg3 = "(ON|OFF),0,0,0,(\\d+),*(IN|OUT)*";
		Matcher m1 = Pattern.compile(reg1).matcher(msg.content);
		Matcher m2 = Pattern.compile(reg2).matcher(msg.content);
		Matcher m3 = Pattern.compile(reg3).matcher(msg.content);		
		if(m1.find()){
			Matcher m = m1;
			double lan1 = get(m.group(2), m.group(3));
			double lon1 = get(m.group(4), m.group(5));
			double lan2 = get(m.group(6), m.group(7));
			double lon2 = get(m.group(8), m.group(9));
			fence = new FenceRectangle(lan1, lon1, lan2, lon2);			
			fence.setState(m.group(1));			
			fence.setType(m.group(10));
		}else if(m2.find()){
			Matcher m = m2;
			double lan = get(m.group(2), m.group(3));
			double lon = get(m.group(4), m.group(5));
			int radio = Integer.parseInt(m.group(6))*100;
			fence = new FenceCircle(radio, lan, lon);
			fence.setState(m.group(1));				
			fence.setType(m.group(7));
		}else if(m3.find()){
			Matcher m = m3;
			int radio = Integer.parseInt(m.group(2))*100;
			fence = new FenceCircle(radio, 0, 0);
			fence.setState(m.group(1));				
			fence.setType(m.group(3));
		}
		return fence;
	}
}
