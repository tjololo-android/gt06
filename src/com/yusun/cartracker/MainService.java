package com.yusun.cartracker;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.netty.NettyClient;
import com.yusun.cartracker.netty.NettyServer;
import com.yusun.cartracker.position.NetworkManager;
import com.yusun.cartracker.position.NetworkManager.NetworkHandler;
import com.yusun.cartracker.position.PositionWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
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
	        startWorkThread();
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
		stopWork();
	}
	void startWorkThread(){
		new Thread("work"){
			public void run(){
				Looper.prepare();				
				startWork();
				Looper.loop();
			}
		}.start();       
	}
	void startWork() {
		logger.info("startWork");
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire(3*60*1000);
        
        Hardware.instance().setmContext(getApplicationContext());
        Hardware.instance().init();
        Settings.instance().setmContext(getApplicationContext());
        Settings.instance().init();
        
	    networkManager = new NetworkManager(this, this);
	    networkManager.start();
        positionWriter = new PositionWriter(this);
		positionWriter.start();
		
		client = new NettyClient();
				
		AppContext.instance().setContext(MainService.this.getApplicationContext());
		AppContext.instance().init();
		AppContext.instance().setClient(client);
		AppContext.instance().setNetWorkManager(networkManager);

//		NettyServer server = new NettyServer();
//		server.start();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			
		
		if(networkManager.isOnline()){
			client.start();
		}
	}

	void stopWork() {
		logger.info("stopWork");
		if(null != positionWriter){
			positionWriter.stop();
		}
		if(null != networkManager){
			networkManager.stop();
		}
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
		if(null != client && isOnline && !AppContext.instance().getProtocol().isOnline()){
			client.start();
		}
	}
}
