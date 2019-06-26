package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.SmsSender;

public class SMS {
	int errorCode =0;
	String content;
	String phoneNum;               
	SMS(String num, String msg){
		phoneNum = num;
		content = msg;
	}
	public void sendAck(String msg){
		sendSms(phoneNum, msg);
	}	
	public static SMS fromSms(String num, String msg) {
		String reg = "[A-Z]+,[0-9]{6}.*#";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(msg);
		if(m.find()){
			return new SMS(num, msg);
		}
		return null;
	}
	public static void sendSms(String num, String con){
		SmsSender.send(num, con);
	}
}
