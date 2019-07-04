package com.yusun.cartracker.model.sms.abs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMS {
	int errorCode =0;
	String cmd;
	public String content;
	String phoneNum;               
	SMS(String num, String cmd, String msg){
		this.cmd = cmd;
		phoneNum = num;
		content = msg;
	}
	public void sendAck(String msg){
		sendSms(phoneNum, msg);
	}
	public void sendFormatErr(){
		sendAck("format error!");
	}		
	public void sendPassErr() {
		sendAck("password error!");		
	}
	public void sendOK() {
		sendAck("OK");		
	}
	public void sendErr() {
		sendAck("ERROR");		
	}
	public static SMS fromSms(String num, String msg) {
		String reg = "([A-Z]+),[0-9]{6},*(.*)#";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(msg);		
		if(m.find()){
			return new SMS(num, m.group(1), m.group(2));
		}
		return null;
	}
	public static void sendSms(String num, String con){
		SmsSender.send(num, con);
	}
	public String getPhoneNum() {
		return phoneNum;
	}

}
