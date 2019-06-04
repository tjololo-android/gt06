package com.yusun.cartracker;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.netty.NettyClient;
import com.yusun.cartracker.netty.NettyServer;
import com.yusun.cartracker.position.NetworkManager;
import com.yusun.cartracker.position.NetworkManager.NetworkHandler;
import com.yusun.cartracker.position.PositionWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MainService extends Service implements NetworkHandler {

	  Logger logger = new Logger(MainService.class);
	  
	    private PowerManager.WakeLock wakeLock;
	    
	    private NettyClient client;
	    
	    private NetworkManager networkManager;
	    
	    private PositionWriter positionWriter;
	   
	    @Override
	    public void onCreate() {
	        logger.info("onCreate");
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
            Hardware.instance().setmContext(getApplicationContext());
            Hardware.instance().init();
            
            startWork();
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

		stopWork();
	}

	void startWork() {
		logger.info("startWork");
	    networkManager = new NetworkManager(this, this);
        positionWriter = new PositionWriter(this);
		HandlerThread ht = new HandlerThread("worker");
		ht.start();
		Handler handler = new Handler(ht.getLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {			
				positionWriter.start();
				AppContext.instance().setContext(MainService.this.getApplicationContext());
				AppContext.instance().init();

//				 NettyServer server = new NettyServer();
//				 server.start();
//
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				client = new NettyClient("www.18gps.net", 7018);				
				AppContext.instance().setClient(client);
				if(networkManager.isOnline()){
					client.start();
				}
				networkManager.start();
			}
		});
	}

	void stopWork() {
		logger.info("stopWork");
		positionWriter.stop();

		if (client != null) {
			client.stop();
		}
		
		AppContext.instance().uninit();

		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
	}
	@Override
	public void onNetworkUpdate(boolean isOnline) {
		if(isOnline && !AppContext.instance().getProtocol().isOnline()){
			client.start();
		}
	}
}
