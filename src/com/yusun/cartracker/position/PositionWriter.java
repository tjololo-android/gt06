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

import com.yusun.cartracker.util.Logger;

import android.content.Context;
import android.os.Handler;

public class PositionWriter implements PositionProvider.PositionListener {
    
    Logger logger = new Logger(PositionWriter.class);    

    private Handler handler;
    private PositionProvider positionProvider;
    private DatabaseHelper databaseHelper;


    public PositionWriter(Context context) {        
        handler = new Handler();
        positionProvider = PositionProviderFactory.create(context, this);
        databaseHelper = new DatabaseHelper(context);          
    }

    public void start() {  
        try {
        	logger.info("start");
            positionProvider.startUpdates();
        } catch (SecurityException e) {
            logger.error("start exception", e);
        }       
    }

    public void stop() {      
        try {
        	logger.info("stop");
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
            }
        });
    }
}
