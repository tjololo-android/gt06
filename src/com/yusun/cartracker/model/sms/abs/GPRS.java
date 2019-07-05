package com.yusun.cartracker.model.sms.abs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.protocol.MessageCommandReply;

public class GPRS extends MSG{
	private int serverFlag;
	GPRS(String cmd, String content, int serverFlag){
		super(cmd, content);
		this.serverFlag = serverFlag;
	}
	public void sendAck(String msg){
		sendMsg(content, serverFlag);
	}
	@Override
	public String getPhoneNum() {		
		return "00000000";
	}
	public static void sendMsg(String content, int flag){
		MessageCommandReply msgReply = new MessageCommandReply(content, flag);
		msgReply.sendToTarget();
	}
	public static GPRS fromMsg(String msg, int flag){
		String reg = "([A-Z]+),*(.*)*";		
		Matcher m = Pattern.compile(reg).matcher(msg);		
		if(m.find()){
			return new GPRS(m.group(1), m.group(2), flag);
		}
		return null;
	}
}
