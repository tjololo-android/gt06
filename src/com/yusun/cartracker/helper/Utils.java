package com.yusun.cartracker.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Build;

public class Utils {
	public static boolean isValidVrl(String str){
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https��http��ftp��rtsp��mms
                + "?(([0-9a-z_!~*��().&=+$%-]+: )?[0-9a-z_!~*��().&=+$%-]+@)?" //ftp��user@  
               + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP��ʽ��URL- ���磺199.194.52.184  
                 + "|" // ����IP��DOMAIN��������
                 + "([0-9a-z_!~*��()-]+\\.)*" // ����- www.  
                 + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // ��������  
                + "[a-z]{2,6})" // first level domain- .com or .museum  
                + "(:[0-9]{1,5})?" // �˿ں����Ϊ65535,5λ��
                + "((/?)|" // a slash isn��t required if there is no file name  
                + "(/[0-9a-z_!~*��().;?:@&=+$,%#-]+)+/?)$";  
        return  str.matches(regex);
	}	
	public static String join(String[] str, String flag){
		StringBuilder sb = new StringBuilder();
		for(String s : str){
			sb.append(s + flag);
		}
		String strline = sb.toString(); 
		return strline.substring(0, strline.length()-flag.length());
	}
	public static String getDataTime(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	public static boolean isFeatures() {
		return Build.MANUFACTURER.contains("unknow");
//	    return Build.FINGERPRINT.startsWith("generic")
//	            || Build.FINGERPRINT.toLowerCase().contains("vbox")
//	            || Build.FINGERPRINT.toLowerCase().contains("test-keys")
//	            || Build.MODEL.contains("google_sdk")
//	            || Build.MODEL.contains("Emulator")
//	            || Build.MODEL.contains("Android SDK built for x86")
//	            || Build.MANUFACTURER.contains("Genymotion")
//	            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
//	            || "google_sdk".equals(Build.PRODUCT);
	}
}
