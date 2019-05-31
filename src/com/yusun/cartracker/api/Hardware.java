package com.yusun.cartracker.api;

import com.yusun.cartracker.model.Position;
import com.yusun.cartracker.model.TimeZone;

public class Hardware {
	public static String getImei(){
		return "860016020783556";
	}
	public static String getDeviceType() {	
		return "11";
	}
	public static TimeZone getTimeZone() {
		return null;
	}
	public static int getLanguage() {
		return 1;
	}
	public static void rebootOnTime() {
		
	}	
	public static boolean isElectronicOn(){
		return true;
	}
	public static boolean isGpsFixed(){
		return true;
	}
	public static boolean isRecharge(){
		return true;
	}
	public static boolean isAccon(){
		return true;
	}
	public static boolean isInguard(){
		return true;
	}
	public static int geValtage(){
		return 456;
	}
	public static int getSignal(){
		return 5;
	}
	public static int getmcc(){
		return 100;
	}
	public static int getmnc(){
		return 100;
	}
	public static int getlac(){
		return 100;
	}
	public static int getcellId(){
		return 100;
	}
	public static Position getPosition(){
		return null;
	}
}
