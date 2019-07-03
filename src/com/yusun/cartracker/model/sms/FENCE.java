package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.Fence;
import com.yusun.cartracker.model.FenceCircle;
import com.yusun.cartracker.model.FenceRectangle;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

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
	public void doCmd(SMS msg) {				
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
			msg.sendAck("format error");
		}
	}	
	Fence getFence(SMS msg){
		Fence fence = null;
		String reg1 = "(ON|OFF),1,([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),*(IN|OUT)*";
		String reg2 = "(ON|OFF),0,([NS])(\\d+\\.*\\d*),([WE])(\\d+\\.*\\d*),(\\d+),*(IN|OUT)*";
		String reg3 = "(ON|OFF),0,0,0,(\\d+),*(IN|OUT)*";		
		if(msg.content.matches(reg1)){
			Pattern p = Pattern.compile(reg1);
			Matcher m = p.matcher(msg.content);
			if(m.find()){
				int c = m.groupCount();
				String s = m.group();
				String s0 = m.group(0);
				
				double lan1 = get(m.group(2), m.group(3));
				double lon1 = get(m.group(4), m.group(5));
				double lan2 = get(m.group(6), m.group(7));
				double lon2 = get(m.group(8), m.group(9));
				fence = new FenceRectangle(lan1, lon1, lan2, lon2);			
				fence.setState(m.group(1));			
				fence.setType(m.group(10));				
			}
		}else if(msg.content.matches(reg2)){
			Pattern p = Pattern.compile(reg2);
			Matcher m = p.matcher(msg.content);
			if(m.find()){
				double lan = get(m.group(3), m.group(4));
				double lon = get(m.group(5), m.group(6));
				int radio = Integer.parseInt(m.group(7))*100;
				fence = new FenceCircle(radio, lan, lon);
				fence.setState(m.group(1));				
				fence.setType(m.group(8));				
			}
		}else if(msg.content.matches(reg3)){
			Pattern p = Pattern.compile(reg3);
			Matcher m = p.matcher(msg.content);
			if(m.find()){
				int radio = Integer.parseInt(m.group(2));
				fence = new FenceCircle(radio, 0, 0);
				fence.setState(m.group(1));				
				fence.setType(m.group(3));				
			}
		}
		return fence;
	}
}
