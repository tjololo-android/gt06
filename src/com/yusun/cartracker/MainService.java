package com.yusun.cartracker;

import com.yusun.cartracker.netty.NettyClient;
import com.yusun.cartracker.netty.NettyServer;
import com.yusun.cartracker.util.Logger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MainService extends Service {

	  Logger logger = new Logger(MainService.class);
	  
	    private PowerManager.WakeLock wakeLock;
	    
	    private NettyClient client;
	   
	    @Override
	    public void onCreate() {
	        logger.info("onCreate");
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
            
            HandlerThread ht = new HandlerThread("worker");
            ht.start();
            Handler handler = new Handler(ht.getLooper());
            handler.post(new Runnable(){            
            	@Override
            	public void run(){
		            AppContext.instance().setContext(MainService.this.getApplicationContext());
		            AppContext.instance().init();
		            
		            //NettyServer server = new NettyServer();
		            //server.start();
		            
		            try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		            client = new NettyClient("www.18gps.net", 7018);
		            //client = new NettyClient("127.0.0.1", 7018);
		            client.start();  
		            
		            
		            AppContext.instance().setClient(client);  
		            AppContext.instance().getmProtocolMgr().getmProtocol().start();            
            	}
            });
        }
	    @Override
	    public IBinder onBind(Intent intent) {
	    	logger.info("onBind");
	        return null;
	    }

	   
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {	 
	    	logger.info("onStartCommand");
	        return START_STICKY;
	    }

	    @Override
	    public void onDestroy() {
	    	logger.info("onDestroy");	

	        stopForeground(true);

	        if (wakeLock != null && wakeLock.isHeld()) {
	            wakeLock.release();
	        }
	        if (client != null) {
	        	client.stop();
	        }
	    }  
}
