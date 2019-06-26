/*
 * Copyright 2012 - 2017 Anton Tananaev (anton@traccar.org)
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
package com.yusun.cartracker;

import java.util.Random;

import com.yusun.cartracker.helper.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

public class MainFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int ALARM_MANAGER_INTERVAL = 15000;

    public static final String KEY_DEVICE = "id";
    public static final String KEY_URL = "url";
    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_ANGLE = "angle";
    public static final String KEY_ACCURACY = "accuracy";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LBS_INTERVAL = "lbs_interval";
    public static final String KEY_GPS_WORK_INTERVAL = "gps_work_interval";
    public static final String KEY_APN_NAME = "apn_name";
    public static final String KEY_APN_USER = "apn_user";
    public static final String KEY_APN_PASSWORD = "apn_password";
    public static final String KEY_APN_NETID = "apn_netid";
    public static final String KEY_APN_OPERATOR_IP = "apn_operator_ip";

    private static final int PERMISSIONS_REQUEST_LOCATION = 2;

    private SharedPreferences sharedPreferences;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addPreferencesFromResource(R.xml.preferences);
        initPreferences();

        findPreference(KEY_DEVICE).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return newValue != null && !newValue.equals("");
            }
        });
        findPreference(KEY_URL).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {            	
                return (newValue != null) && validateServerURL(newValue.toString());
            }
        });

        findPreference(KEY_INTERVAL).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue != null) {
                    try {
                        int value = Integer.parseInt((String) newValue);
                        return value > 0;
                    } catch (NumberFormatException e) {
                        Log.w(TAG, e);
                    }
                }
                return false;
            }
        });

        Preference.OnPreferenceChangeListener numberValidationListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue != null) {
                    try {
                        int value = Integer.parseInt((String) newValue);
                        return value >= 0;
                    } catch (NumberFormatException e) {
                        Log.w(TAG, e);
                    }
                }
                return false;
            }
        };
        findPreference(KEY_DISTANCE).setOnPreferenceChangeListener(numberValidationListener);
        findPreference(KEY_ANGLE).setOnPreferenceChangeListener(numberValidationListener);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), BootCompleteReceiver.class), 0);

        if (sharedPreferences.getBoolean(KEY_STATUS, false)) {
            startTrackingService(true, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setPreferencesEnabled(boolean enabled) {
        findPreference(KEY_DEVICE).setEnabled(enabled);
        findPreference(KEY_URL).setEnabled(enabled);
        findPreference(KEY_INTERVAL).setEnabled(enabled);
        findPreference(KEY_DISTANCE).setEnabled(enabled);
        findPreference(KEY_ANGLE).setEnabled(enabled);
        findPreference(KEY_ACCURACY).setEnabled(enabled);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_STATUS)) {
            if (sharedPreferences.getBoolean(KEY_STATUS, false)) {
                startTrackingService(true, false);
            } else {
                stopTrackingService();
            }
        } else if (key.equals(KEY_DEVICE)) {
            findPreference(KEY_DEVICE).setSummary(sharedPreferences.getString(KEY_DEVICE, null));
        }
    }


    private void initPreferences() {
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        if (!sharedPreferences.contains(KEY_DEVICE)) {
            String id = String.valueOf(new Random().nextInt(900000) + 100000);
            sharedPreferences.edit().putString(KEY_DEVICE, id).apply();
            ((EditTextPreference) findPreference(KEY_DEVICE)).setText(id);
        }
        findPreference(KEY_DEVICE).setSummary(sharedPreferences.getString(KEY_DEVICE, null));
    }

    private void startTrackingService(boolean checkPermission, boolean permission) {
//        if (checkPermission) {
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                permission = true;
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
//                }
//                return;
//            }
//        }
//
//        if (permission) {
//            setPreferencesEnabled(false);
//            ContextCompat.startForegroundService(getActivity(), new Intent(getActivity(), MainService.class));
//            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    ALARM_MANAGER_INTERVAL, ALARM_MANAGER_INTERVAL, alarmIntent);
//        } else {
//            sharedPreferences.edit().putBoolean(KEY_STATUS, false).apply();
//            TwoStatePreference preference = (TwoStatePreference) findPreference(KEY_STATUS);
//            preference.setChecked(false);
//        }
    	
      setPreferencesEnabled(false);
      getActivity().startService(new Intent(getActivity(), MainService.class));
      alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
              ALARM_MANAGER_INTERVAL, ALARM_MANAGER_INTERVAL, alarmIntent);
    }

    private void stopTrackingService() {
        alarmManager.cancel(alarmIntent);
        getActivity().stopService(new Intent(getActivity(), MainService.class));
        setPreferencesEnabled(true);
    }
    public boolean validateServerURL(String userUrl) {
        if(Utils.isValidVrl(userUrl)){
        	return true;
        }
        Toast.makeText(getActivity(), R.string.error_msg_invalid_url, Toast.LENGTH_LONG).show();
        return false;
    }

}
