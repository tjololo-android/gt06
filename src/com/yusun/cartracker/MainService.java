package com.yusun.cartracker;

import com.yusun.cartracker.netty.NettyClient;
import com.yusun.cartracker.netty.NettyServer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MainService extends Service {

	  private static final String TAG = MainService.class.getSimpleName();

	    private PowerManager.WakeLock wakeLock;
	    //private Client client;
	    
	    private NettyClient client;
	   
	    @Override
	    public void onCreate() {
	        Log.i(TAG, "service create");
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
            
            AppContext.instance().init();
            
            //client = new Client();
            //client.start("www.18gps.net", 7018);
            NettyServer server = new NettyServer();
            server.start();
            
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            client = new NettyClient();
            client.start();  
            
            
            AppContext.instance().setClient(client);  
            AppContext.instance().getmProtocolMgr().getmProtocol().start();
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
