package com.yusun.cartracker.model.sms.abs;

public abstract class MSG {	
	public String cmd;
	public String content;	           
	MSG(String cmd, String content){
		this.cmd = cmd;
		this.content = content;
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
	public abstract void sendAck(String msg);
	public abstract String getPhoneNum();
}
