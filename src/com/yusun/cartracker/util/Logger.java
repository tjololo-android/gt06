package com.yusun.cartracker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class Logger {
	private Class<?> clazz;
	 
	static Socket mSocket;
	 
	static String mLogFile = Environment.getExternalStorageDirectory() + "/"+System.currentTimeMillis()+"Service.log";
	static FileOutputStream mOut=null;
	static boolean mLogToFile=false;
	static int mMode=0;
	public static void logToFile(boolean toFile){		
		mLogToFile = toFile;		
	}
	
	static boolean createLogFile()
	{
		
		if(mLogToFile==false)return false;
		
		if(mOut==null){	
			File file=new File(mLogFile);	
			//file.delete();
			
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				mOut=new FileOutputStream(file,true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
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
	
	public void detail(String mess) {
		if(mMode==1){
			info(mess);
		}
		
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
		
	   if(mSocket!=null){
			try{
				PrintWriter out = new PrintWriter(mSocket.getOutputStream());
			    out.println(msg);
			    out.flush();
			}catch(IOException e){
			       e.printStackTrace();
			}
		}
	}
		 
	
	public void startService()
	{
		new Thread(new Runnable() {
			
	        @Override  
	        public void run() {
	        
	        //	logToFile(true);
	        	createLogFile();
	        	 SocketServer server = new SocketServer(1234);
	        	 if(server!=null)
	    		 server.beginListen();
	        }  
	    }).start();  
		
		
		
		 
	}
	 public class SocketServer {
	    
	    ServerSocket sever;
	   
	    public SocketServer(int port){
	        try{
	            sever = new ServerSocket(port);
	        }catch(IOException e){
	            e.printStackTrace();
	        }
	    }
	    
	    public void beginListen(){
	        while(true){
	            try{
	            	if(sever==null)return;
	            	mSocket = sever.accept();
	                
	                new Thread(new Runnable(){
	                    public void run(){
	                        BufferedReader in;
	                        try{
	                            in = new BufferedReader(new InputStreamReader(mSocket.getInputStream(),"UTF-8"));
	                            PrintWriter out = new PrintWriter(mSocket.getOutputStream());
	                            while (!mSocket.isClosed()){
	                                String str;
	                                str = in.readLine();
	                                
	                                if(str.startsWith("1")){
	                                	
	                                	mMode=1;
	                                }else if(str.startsWith("0")){
	                                	mMode=0;
	                                }
 
	                                out.println("Hello!world!! " + str);
	                                out.flush();
	                                if (str == null || str.equals("end"))
	                                    break;
	                                System.out.println(str);
	                            }
	                            mSocket.close();
	                            mSocket=null;
	                        }catch(IOException e){
	                            e.printStackTrace();
	                        }
	                    }
	                }).start();
	            }catch(IOException e){
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	 

}
