/*
 * Copyright 2015 - 2019 Anton Tananaev (anton@traccar.org)
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

import java.util.Date;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

public class TrackingController implements PositionProvider.PositionListener, NetworkManager.NetworkHandler {

    private static final int RETRY_DELAY = 30 * 1000;
    private static final int WAKE_LOCK_TIMEOUT = 120 * 1000;
    
    Logger logger = new Logger(TrackingController.class);

    private boolean isOnline;
    private boolean isWaiting;

    private Context context;
    private Handler handler;
    private SharedPreferences preferences;

    private String url;

    private PositionProvider positionProvider;
    private DatabaseHelper databaseHelper;
    private NetworkManager networkManager;

    public TrackingController(Context context) {
        this.context = context;      
        handler = new Handler();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        positionProvider = PositionProviderFactory.create(context, this);
        databaseHelper = new DatabaseHelper(context);
        networkManager = new NetworkManager(context, this);
        isOnline = networkManager.isOnline();      
    }

    public void start() {
        if (isOnline) {
            read();
        }
        try {
            positionProvider.startUpdates();
        } catch (SecurityException e) {
            logger.error("start exception", e);
        }
        networkManager.start();
    }

    public void stop() {
        networkManager.stop();
        try {
            positionProvider.stopUpdates();
        } catch (SecurityException e) {
            logger.error("stop exception", e);
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPositionUpdate(Position position) {
     
        if (position != null) {
            write(position);
        }
    }

    @Override
    public void onPositionError(Throwable error) {
    	logger.error("requestLocationUpdates exception", error);
    }

    @Override
    public void onNetworkUpdate(boolean isOnline) {     
        if (!this.isOnline && isOnline) {
            read();
        }
        this.isOnline = isOnline;
    }

    //
    // State transition examples:
    //
    // write -> read -> send -> delete -> read
    //
    // read -> send -> retry -> read -> send
    //

    private void log(String action, Position position) {
        if (position != null) {
            action += " (" +
                    "id:" + position.getId() +
                    " time:" + position.getTime().getTime() / 1000 +
                    " lat:" + position.getLatitude() +
                    " lon:" + position.getLongitude() + ")";
        }
        logger.info(action);
    }

    private void write(Position position) {
        log("write", position);
        databaseHelper.insertPositionAsync(position, new DatabaseHelper.DatabaseHandler<Void>() {
            @Override
            public void onComplete(boolean success, Void result) {
                if (success) {
                    if (isOnline && isWaiting) {
                        read();
                        isWaiting = false;
                    }
                }
            }
        });
    }

    private void read() {
        log("read", null);
        databaseHelper.selectPositionAsync(new DatabaseHelper.DatabaseHandler<Position>() {
            @Override
            public void onComplete(boolean success, Position result) {
                if (success) {
                    if (result != null) {                        
                        send(result);                        
                    } else {
                    	//send(getFackPos());
                        isWaiting = true;
                    }
                } else {
                    retry();
                }
            }
        });
    }
    
    private void delete(Position position) {
        log("delete", position);
        databaseHelper.deletePositionAsync(position.getId(), new DatabaseHelper.DatabaseHandler<Void>() {
            @Override
            public void onComplete(boolean success, Void result) {
                if (success) {
                    read();
                } else {
                    retry();
                }
            }
        });
    }

    private void send(final Position position) {
//        log("send", position);
//        String request = ProtocolFormatter.formatRequest(url, position);
//        RequestManager.sendRequestAsync(request, new RequestManager.RequestHandler() {
//            @Override
//            public void onComplete(boolean success) {
//                if (success) {
//                    delete(position);
//                } else {
//                    retry();
//                }
//            }
//        });
    }

    private void retry() {
        log("retry", null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline) {
                    read();
                }
            }
        }, RETRY_DELAY);
    }
}
