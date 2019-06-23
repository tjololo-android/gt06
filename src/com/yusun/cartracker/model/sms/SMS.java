package com.yusun.cartracker.model.sms;
public class SMS {
	int errorCode =0;
	String command;
	
	
	SMS(String msg)
	{
		
	}
	public String formAck(String msg)
	{
		return msg;
	}
	
	public void sendAck(int errCode,String msg){
		errorCode = errCode;
	}
	
	public void sendAck(String msg){
	}
	public static boolean isCommand(String msg){
		return true;
	}
	public static SMS fromSms(String msg) {		
		return null;
	}
}
