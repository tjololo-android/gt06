package com.yusun.cartracker.model.sms;

import com.yusun.cartracker.AppContext;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Verson implements CmdHandler{

	@Override
	public String getCmd() {
		return CMDS.VERSION;
	}

	@Override
	public void doCmd(SMS msg) {
		int versioncode=0;
	    String versionname="unknow";
	    Context ctx = AppContext.instance().getContext();
	    PackageManager pm = ctx.getPackageManager();		 
        PackageInfo packageInfo;
		try {
			packageInfo = pm.getPackageInfo(ctx.getPackageName(), 0);
			versioncode = packageInfo.versionCode;
	        versionname = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        	
		msg.sendAck("versionName="+versionname + " versionCode="+versioncode);
	}	
}
