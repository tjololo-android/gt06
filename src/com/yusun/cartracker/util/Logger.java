package com.yusun.cartracker.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class Logger {
	private Class<?> clazz = Logger.class;	 
	static String mLogFile = Environment.getExternalStorageDirectory() + "/"+System.currentTimeMillis()+"tracker.log";
	static FileOutputStream mOut=null;
	static boolean mLogToFile=false;
	static boolean debug = true;
	public static void logToFile(boolean toFile){		
		mLogToFile = toFile;		
	}
	
	static boolean createLogFile()
	{
		
		if(mLogToFile==false)return false;
		
		if(mOut==null){	
			File file=new File(mLogFile);	
			
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				mOut=new FileOutputStream(file,true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	
	
	static void writeToLogFile(String str){
		
		if(!mLogToFile)return ;
		
		try {			
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
			String simpleDate = simpleDateFormat.format(new Date(System.currentTimeMillis())); 
			mOut.write((simpleDate+"  :  "+str+"\r\n").getBytes());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("write log error");
		}
	}
	
	
	public Logger(Class<?> clazz){
		this.clazz = clazz;
	}	

	public void info(String mess){
		sendLogString(clazz.getName()+":"+ mess+"\r\n");
		Log.i(clazz.getName(),""+ mess);
	}
	
	public void info(String mess, Throwable tr){
		Log.i(clazz.getName(), ""+mess, tr);
	}
	
	public void debug(String mess){
		sendLogString(clazz.getName()+":"+ mess+"\r\n");
		Log.d(clazz.getName(),""+ mess);
	}
	
	public void debug(String msg,Throwable tr){
		Log.d(clazz.getName(), ""+msg,tr);
	}
	
	public void error(String msg){
		sendLogString(clazz.getName()+":"+ msg+"\r\n");
		Log.e(clazz.getName(), ""+msg);
	}
	
	public void error(String msg, Throwable tr){
		Log.e(clazz.getName(), ""+msg, tr);
	}
	
	public void warning(String msg){
		sendLogString(clazz.getName()+":"+ msg+"\r\n");
		Log.w(clazz.getName(), ""+msg);
	}
	
	public void warning(String msg, Throwable tr){
		Log.w(clazz.getName(), ""+msg, tr);
	}
	
	public String getStackTraceString(Throwable tr){
		return Log.getStackTraceString(tr);
	}
	
	
	void sendLogString(String msg)
	{
		writeToLogFile(msg);
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
		String simpleDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));  

		msg="["+simpleDate+"]"+msg;		

	}
	
	public void printFuncName(){
		try{
			String name = Thread.currentThread().getStackTrace()[3].getMethodName();
			info(name);
		}catch(Exception e){
			
		}
	}
}
