package com.yusun.cartracker.model.sms.abs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;

public class SMS extends MSG{	
	String phoneNum;               
	SMS(String cmd, String msg, String num){
		super(cmd, msg);
		phoneNum = num;		
	}
	public void sendAck(String msg){
		sendMsg(phoneNum, msg);
	}
	public static SMS fromMsg(String num, String msg) {
		String reg = "([A-Z]+),([0-9]{6}),*(.*)#";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(msg);		
		if(m.find()){
			if(Settings.instance().checkPass(m.group(2))
				|| ("RECOVER".equals(m.group(1)) && Settings.instance().checkAdminPass(m.group(2)))){
				return new SMS(m.group(1), m.group(3), num);
			}else{
				sendMsg(num, "password error!");
			}
		}else{
			sendMsg(num, "format error!");
		}
		return null;
	}
	public static void sendMsg(String num, String con){
		SmsSender.send(num, con);
	}
	public String getPhoneNum() {
		return phoneNum;
	}

}
