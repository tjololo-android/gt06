package com.yusun.cartracker.helper;

public class Utils {
	public static boolean isValidVrl(String str){
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
                + "?(([0-9a-z_!~*‘().&=+$%-]+: )?[0-9a-z_!~*‘().&=+$%-]+@)?" //ftp的user@  
               + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184  
                 + "|" // 允许IP和DOMAIN（域名）
                 + "([0-9a-z_!~*‘()-]+\\.)*" // 域名- www.  
                 + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名  
                + "[a-z]{2,6})" // first level domain- .com or .museum  
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn‘t required if there is no file name  
                + "(/[0-9a-z_!~*‘().;?:@&=+$,%#-]+)+/?)$";  
        return  str.matches(regex);
	}	
}
