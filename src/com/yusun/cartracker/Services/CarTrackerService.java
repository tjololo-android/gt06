package com.yusun.cartracker.Services;

import com.yusun.cartracker.netty.Client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class CarTrackerService extends Service {

	  private static final String TAG = CarTrackerService.class.getSimpleName();

	    private PowerManager.WakeLock wakeLock;
	    private Client client;
	   
	    @Override
	    public void onCreate() {
	        Log.i(TAG, "service create");

            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();

            client = new Client();
            client.start("www.18gps.net", 7018);
	    }

	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }

	   
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {	 
	        return START_STICKY;
	    }

	    @Override
	    public void onDestroy() {
	        Log.i(TAG, "service destroy");	

	        stopForeground(true);

	        if (wakeLock != null && wakeLock.isHeld()) {
	            wakeLock.release();
	        }
	        if (client != null) {
	        	client.stop();
	        }
	    }
}
