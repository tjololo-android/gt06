package com.yusun.cartracker.model.sms;

import java.util.HashMap;

import org.json.JSONObject;

public class SmsManager {

	static HashMap<String, CmdHandler> mHandler = new HashMap<String, CmdHandler>();

	static void reg(CmdHandler handler) {
		mHandler.put(handler.getCmd().toLowerCase(), handler);
	}

	static public void addReq(String msg) {
		SMS sMsg = SMS.fromSms(msg);		
		if(null != sMsg){
			handle(sMsg);
		}
	}

	public static void handle(SMS sMsg) {

		String cmd[] = sMsg.command.split(" ");
		CmdHandler handler = mHandler.get(cmd[0].toLowerCase());

		if (handler != null) {
			handler.doCmd(sMsg);
		} else {
			sMsg.sendAck("unknow command " + cmd[0]);
		}
	}

	public static void init() {
		reg(new Versin());
	}
}