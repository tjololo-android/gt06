package com.yusun.cartracker;

import java.util.Timer;
import java.util.TimerTask;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.helper.Logger;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
/*
 * if system in deep sleep mode,the motion sensor will be paused. 
 * then how to wake up system use motion sensor? using motion sensor interrupt pin wake up the system and  then how check the level? 
 * 
 * */
public class MotionMgr {

	private static final Logger LOGGER = new Logger(MotionMgr.class);
	private final static int COUNT_MAX = 10;

	private XYZS mXyz = new XYZS();
	private Shock mShock = new Shock();
	private SensorManager mSensorManager;
	private Sensor mSensor;

	private static Context mContext;

	private static float maxDertaXyz;

	public static int mShockLevel = 5;

	public static boolean mStill = false;	

	static private final double motionLevelArray[] = {0.60,1.0,2.0,3.0,4,5,6,7,8,10,20,30,50,90,100};
	
	// 0 1 2 3 4 5 6 7 8 9 10                    
	static private final double motionShockArray[] = {196,225,256,289,324,361,400,625,900,1600,3000};
	
	public static long m_time_shock_raised = -1;
	public static long m_time_lastmoving = -1;

	public MotionMgr(Context context) {
		mContext = context;
	}

	private void enterStillState() {
		if (mStill)
			return;
		mStill = true;
	}

	private void enterMovingState() {
		if (!mStill)
			return;
		mStill = false;
	}
	public boolean isStill(){
		return mStill;
	}
	public void start() {
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(eventListener, mSensor, 100 * 1000);
	}
	public void stop(){
		mSensorManager.unregisterListener(eventListener);
	}

	public static float getDertaXyz() {
		return (float) Math.sqrt(maxDertaXyz);
	}

	void checkIfSendAlarm() {

		LOGGER.info("checkIfSendAlarm");

		if (SystemClock.uptimeMillis() - m_time_shock_raised > 60 * 1000) {
			// now in the work mode state
			m_time_shock_raised = SystemClock.uptimeMillis();
			mShock.onShock();
		}

	}

	SensorEventListener eventListener = new SensorEventListener() {
		int index = 0;

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mXyz.add(event.values[0], event.values[1], event.values[2]);
			if(index++ == COUNT_MAX){				
				float derta = mXyz.getMaxDertaXYZ();
				index = 0;
				mXyz.clear();
				
				if (derta > motionLevelArray[0]) {
					m_time_lastmoving = SystemClock.uptimeMillis();
					enterMovingState();
				}
	
				if (SystemClock.uptimeMillis() - m_time_lastmoving > 2 * 60 * 1000) {
					enterStillState();
				}		
	
				if (derta > motionShockArray[0]) {
					getShockLevel();
					LOGGER.info("derta:" + derta + "    mShockLevel:" + mShockLevel);
				}
	
				if (derta > motionShockArray[mShockLevel]) {
					checkIfSendAlarm();
				}
			}
		}
	};
	
	class XYZS {
		int index;
		private float x[] = new float[COUNT_MAX];
		private float y[] = new float[COUNT_MAX];
		private float z[] = new float[COUNT_MAX];

		void add(float x0, float y0, float z0) {
			if(index == COUNT_MAX)
				return;
			
			x[index] = x0;
			y[index] = y0;
			z[index] = z0;
			index++;
		}

		float getDelta2(float a1, float a2) {
			return ((a1 >= a2) ? (a1 - a2) : (a2 - a1));
		}

		float getDeltaXyz(float deltaX, float deltaY, float deltaZ) {
			return (float) Math.sqrt((double) (deltaX * deltaX + deltaY * deltaY));
		}

		float getDeltaXyzPower(float deltaX, float deltaY, float deltaZ) {
			return (float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
		}

		float getDeltaXyzPower2Points(float x0, float y0, float z0, float x1, float y1, float z1) {
			return (float) ((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0) + (z1 - z0) * (z1 - z0));
		}
		public void clear(){
			index = 0;
		}
		public float getMaxDertaXYZ() {

			float maxXyz = 0;

			for (int i = 0; i < index - 1; i++) {
				for (int j = i + 1; j < index; j++) {
					float power = getDeltaXyzPower2Points(x[j], y[j], z[j], x[i], y[i], z[i]);
					if (power > maxXyz)
						maxXyz = power;
				}
			}
			maxDertaXyz = maxXyz;
			return maxXyz;
		}

	}
	static public void getShockLevel() {
		int level = Settings.instance().getCollisionLevel();
		if (level >= 0 && level <= 10) {
			mShockLevel = level;
		} else {
			mShockLevel = 5;
		}
	}
	static public boolean isShockOn(){
		return Settings.instance().getVibration();
	}
	
	class Shock {
		public void onShock(){		
			openGps();		
			reportSharkAlarm();
		}
		void cancelGpsCloseTimer(){
			mGpsCloseTimer.cancel();
		}
		void openGpsCloseTimer(){
			Timer t = new Timer();
			t.schedule(mGpsCloseTimer, Settings.instance().getGpsWorkInterval());
		}
		
		TimerTask mGpsCloseTimer = new TimerTask() {		
			@Override
			public void run() {			
				closeGps();
			}
		};
		
		private void closeGps(){
			Hardware.instance().turnOnGps(false);
		}
		private void openGps(){
			cancelGpsCloseTimer();
			Hardware.instance().turnOnGps(true);
			openGpsCloseTimer();
		}
		private void reportSharkAlarm(){
			Intent i = new Intent(AlarmMgr.ACTION_ALARM);
			i.putExtra(AlarmMgr.ALARM_TYPE, AlarmMgr.ALARM_TYPE_VIBRATION);
			mContext.sendBroadcast(i);
		}	
	}
}
