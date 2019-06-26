package com.yusun.cartracker.helper;

import android.net.Uri;
import android.webkit.URLUtil;

public class Utils {
	public static boolean isValidVrl(String userUrl){
		int port = Uri.parse(userUrl).getPort();
	    if (URLUtil.isValidUrl(userUrl) && (port == -1 || (port > 0 && port <= 65535))
	            && (URLUtil.isHttpUrl(userUrl) || URLUtil.isHttpsUrl(userUrl))) {
	        return true;
	    }        
	    return false;
	}
}

