/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
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

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.Date;

import com.yusun.cartracker.R;
import com.yusun.cartracker.api.Settings;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "traccar.db";
    public static final String TABLE_SET_NAME = "settings";
    public static final String TABLE_POS_NAME = "position";
    private static boolean isFirstInit = false;

    public interface DatabaseHandler<T> {
        void onComplete(boolean success, T result);
    }

    private static abstract class DatabaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

        private DatabaseHandler<T> handler;
        private RuntimeException error;

        public DatabaseAsyncTask(DatabaseHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        protected T doInBackground(Void... params) {
            try {
                return executeMethod();
            } catch (RuntimeException error) {
                this.error = error;
                return null;
            }
        }

        protected abstract T executeMethod();

        @Override
        protected void onPostExecute(T result) {
            handler.onComplete(error == null, result);
        }
    }

    private SQLiteDatabase db;
    private Context mContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        db = getWritableDatabase();
        if(isFirstInit){
        	init();
        	isFirstInit = false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE position (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +             
                "time INTEGER," +
                "latitude REAL," +
                "longitude REAL," +
                "altitude REAL," +
                "speed REAL," +
                "course REAL," +
                "accuracy REAL," +  
                "fixed INTEGER," +  
                "satellates INTEGER," +  
                "mock INTEGER)");
        
        db.execSQL("CREATE TABLE settings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +             
                "key TEXT," +
                "value TEXT )");        
        isFirstInit = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS settings;");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS settings;");
        onCreate(db);
    }

    public void insertPosition(Position position) {
        ContentValues values = new ContentValues();
        values.put("time", position.getTime().getTime());
        values.put("latitude", position.getLatitude());
        values.put("longitude", position.getLongitude());
        values.put("altitude", position.getAltitude());
        values.put("speed", position.getSpeed());
        values.put("course", position.getCourse());
        values.put("accuracy", position.getAccuracy());
        values.put("fixed", position.isFixed() ? 1 : 0);
        values.put("satellates", position.getSatellites());
        values.put("mock", position.getMock() ? 1 : 0);

        db.insertOrThrow("position", null, values);
    }

    public void insertPositionAsync(final Position position, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                insertPosition(position);
                return null;
            }
        }.execute();
    }

    public Position selectPosition() {
        Position position = new Position();

        Cursor cursor = db.rawQuery("SELECT * FROM position ORDER BY id LIMIT 1", null);
        try {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                position.setId(cursor.getLong(cursor.getColumnIndex("id")));
                position.setTime(new Date(cursor.getLong(cursor.getColumnIndex("time"))));
                position.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                position.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                position.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));
                position.setSpeed(cursor.getDouble(cursor.getColumnIndex("speed")));
                position.setCourse(cursor.getDouble(cursor.getColumnIndex("course")));
                position.setAccuracy(cursor.getDouble(cursor.getColumnIndex("accuracy")));
                position.setFixed(cursor.getInt(cursor.getColumnIndex("fixed")) > 0);
                position.setSatellites(cursor.getInt(cursor.getColumnIndex("satellates")));
                position.setMock(cursor.getInt(cursor.getColumnIndex("mock")) > 0);

            } else {
                return null;
            }
        } finally {
            cursor.close();
        }

        return position;
    }

    public void selectPositionAsync(DatabaseHandler<Position> handler) {
        new DatabaseAsyncTask<Position>(handler) {
            @Override
            protected Position executeMethod() {
                return selectPosition();
            }
        }.execute();
    }

    public void deletePosition(long id) {
        if (db.delete("position", "id = ?", new String[] { String.valueOf(id) }) != 1) {
            throw new SQLException();
        }
    }

    public void deletePositionAsync(final long id, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                deletePosition(id);
                return null;
            }
        }.execute();
    } 


    private void init(){
    	Resources res = mContext.getResources();
    	insert(Settings.LANGUAGE, res.getString(R.string.LANGUAGE));
    	insert(Settings.DEVICETYPE, res.getString(R.string.DEVICETYPE));
    	insert(Settings.SENSOR_INTERVAL, res.getString(R.string.SENSOR_INTERVAL));
    	insert(Settings.SENDS_TIMEOUT, res.getString(R.string.SENDS_TIMEOUT));
    	insert(Settings.DEFENSE_DELAY, res.getString(R.string.DEFENSE_DELAY));
    	insert(Settings.GPS_INTERVAL, res.getString(R.string.GPS_INTERVAL));				
    	insert(Settings.LBS_INTERVAL, res.getString(R.string.LBS_INTERVAL));				
    	insert(Settings.GPS_WORK_INTERVAL, res.getString(R.string.GPS_WORK_INTERVAL));			
    	insert(Settings.SERVICE_IP, res.getString(R.string.SERVICE_IP));
    	insert(Settings.SERVICE_PORT, res.getString(R.string.SERVICE_PORT));
    	insert(Settings.SOS_NUMBER, res.getString(R.string.SOS_NUMBER));
    	insert(Settings.GPS_POWER, res.getString(R.string.GPS_POWER));
    	insert(Settings.PASSWORD, res.getString(R.string.PASSWORD));
    	insert(Settings.ADMIN_PASSWORD, res.getString(R.string.ADMIN_PASSWORD));
    	insert(Settings.MONITOR, res.getString(R.string.MONITOR));
    	insert(Settings.VIRBATION, res.getString(R.string.VIRBATION));
    	insert(Settings.GPS_ANALYSE_URL, res.getString(R.string.GPS_ANALYSE_URL));
    	insert(Settings.FENCE, res.getString(R.string.FENCE));    	
    }
    private void insert(String key, String val){
    	ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("value", val);
		long result = db.insert(TABLE_SET_NAME, null, values);
    }
    public void update(String key, String val) {
        ContentValues values = new ContentValues();
        values.put("key", key);
        values.put("value", val);
        if(-1 == db.update(TABLE_SET_NAME, values, "key=?", new String[]{key})){
        	insert(key, val);
        }
    }
    public String read(String key){
		Cursor cursor = db.query(TABLE_SET_NAME, null, "key = ?", new String[]{key}, null, null, null);
		if(cursor.moveToFirst()){
			return cursor.getString(cursor.getColumnIndex("value"));		
		}
		return null;
    }
    public void resetPassword(){
    	update(Settings.PASSWORD, mContext.getResources().getString(R.string.PASSWORD));
    }
}
