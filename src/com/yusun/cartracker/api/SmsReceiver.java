package com.yusun.cartracker.api;

import com.yusun.cartracker.model.sms.SmsCmdManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {	
		String num, con;	
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] objs = (Object[]) bundle.get("pdus");
			SmsMessage[] smsMessages = new SmsMessage[objs.length];
			for (int i = 0; i < objs.length; i++) {
				smsMessages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
				num = smsMessages[i].getDisplayOriginatingAddress();
				con = smsMessages[i].getDisplayMessageBody();
				SmsCmdManager.addReq(num, con);
				System.out.println("phone:" + num + "content:" + con);	
			}
			abortBroadcast();
		}
	}
}