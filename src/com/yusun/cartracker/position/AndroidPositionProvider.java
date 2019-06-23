/*
 * Copyright 2019 Anton Tananaev (anton@traccar.org)
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.yusun.cartracker.api.Hardware;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

public class AndroidPositionProvider extends PositionProvider implements LocationListener, GpsStatus.Listener{

    private LocationManager locationManager;
    private String provider;
    boolean fixed = true;
    int satellates = 0;

    public AndroidPositionProvider(Context context, PositionListener listener) {
        super(context, listener);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = getProvider("high");
    }

    @SuppressLint("MissingPermission")
    public void startUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    provider, distance > 0 || angle > 0 ? MINIMUM_INTERVAL : interval, 0, this);
            
            locationManager.addGpsStatusListener(this);
        } catch (RuntimeException e) {
            listener.onPositionError(e);
        }
    }

    public void stopUpdates() {
        locationManager.removeUpdates(this);
        locationManager.removeGpsStatusListener(this);
    }

    @SuppressLint("MissingPermission")
    public void requestSingleLocation() {
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                listener.onPositionUpdate(new Position(location, fixed, satellates));
            } else {
                locationManager.requestSingleUpdate(provider, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        listener.onPositionUpdate(new Position(location, fixed, satellates));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, Looper.myLooper());
            }
        } catch (RuntimeException e) {
            listener.onPositionError(e);
        }
    }

    private static String getProvider(String accuracy) {
        if("high".equals(accuracy)) {           
            return LocationManager.GPS_PROVIDER;
        }else if("low".equals(accuracy)){            
            return LocationManager.PASSIVE_PROVIDER;
        }else{
            return LocationManager.NETWORK_PROVIDER;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        processLocation(location, fixed, satellates);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

	@Override
	public void onGpsStatusChanged(int event) {
		if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
			fixed = true;
			Hardware.instance().setGpsFixed(fixed);
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int totalStat = 0;
            int usedStat = 0;
            GpsStatus mGpsStatus = locationManager.getGpsStatus(null);
            Iterable<GpsSatellite> gsiter = mGpsStatus.getSatellites();
            Iterator<GpsSatellite> it = gsiter.iterator();
            Vector<GpsSatellite> vSatellite = new Vector<GpsSatellite>();
            ArrayList<Float> snrarray = new ArrayList<Float>();
            while (it.hasNext()) {
                GpsSatellite sate = (GpsSatellite)(it.next());
                vSatellite.add(sate);
                snrarray.add(sate.getSnr());
                 totalStat++;
                 if (sate.usedInFix()) {
                     usedStat++;
                 }
            }
            satellates = usedStat;
		} else if (event == GpsStatus.GPS_EVENT_STARTED) {

		} else if (event == GpsStatus.GPS_EVENT_STOPPED) {

		}
	}
}
