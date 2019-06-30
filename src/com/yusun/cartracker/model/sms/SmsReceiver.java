package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.helper.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	Logger logger = new Logger(SmsReceiver.class);
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
				AppContext.instance().getSmsCmdManager().addReq(num, con);
				logger.info("phone:" + num + "content:" + con);	
			}
			abortBroadcast();
		}
	}
}