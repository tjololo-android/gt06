package com.yusun.cartracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context paramContext, Intent paramIntent) {
		paramContext.startService(new Intent(paramContext, MainService.class));		
	}
}
