/*
 * Copyright 2013 - 2019 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yusun.cartracker.position;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.util.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.BatteryManager;
import android.preference.PreferenceManager;

public abstract class PositionProvider {

    protected static final int MINIMUM_INTERVAL = 1000;
    
    Logger logger = new Logger(Position.class);

    public interface PositionListener {
        void onPositionUpdate(Position position);
        void onPositionError(Throwable error);
    }

    protected final PositionListener listener;

    protected final Context context;
    protected SharedPreferences preferences;

    protected String deviceId;
    protected long interval;
    protected double distance;
    protected double angle;

    protected Location lastLocation;

    public PositionProvider(Context context, PositionListener listener) {
        this.context = context;
        this.listener = listener;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        deviceId = Hardware.getImei();//preferences.getString(MainFragment.KEY_DEVICE, "860016020783556");
        interval = 3*1000;//Long.parseLong(preferences.getString(MainFragment.KEY_INTERVAL, "600")) * 1000;
        distance = 0;//Integer.parseInt(preferences.getString(MainFragment.KEY_DISTANCE, "0"));
        angle = 0;//Integer.parseInt(preferences.getString(MainFragment.KEY_ANGLE, "0"));
    }

    public abstract void startUpdates();

    public abstract void stopUpdates();

    public abstract void requestSingleLocation();

    protected void processLocation(Location location) {
        if (location != null && (lastLocation == null
                || location.getTime() - lastLocation.getTime() >= interval
                || distance > 0 && location.distanceTo(lastLocation) >= distance
                || angle > 0 && Math.abs(location.getBearing() - lastLocation.getBearing()) >= angle)) {
        	logger.info("location new");
            lastLocation = location;
            listener.onPositionUpdate(new Position(deviceId, location, getBatteryLevel(context)));
        } else {
            logger.info(location != null ? "location ignored" : "location nil");
        }
    }

    protected static double getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            return (level * 100.0) / scale;
        }
        return 0;
    }

}