package com.yusun.cartracker.api;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiInfoMgr{
	public static List<ScanResult> getWifiInfos(Context ctx){
		WifiManager wifiMgr = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
		return wifiMgr.getScanResults(); 
	}	
	/**
	   * 连接有密码的wifi.
	   *
	   * @param SSID     ssid
	   * @param Password Password
	   * @return apConfig
	   */
	  private WifiConfiguration setWifiParamsPassword(String SSID, String Password) {
	    WifiConfiguration apConfig = new WifiConfiguration();
	    apConfig.SSID = "\"" + SSID + "\"";
	    apConfig.preSharedKey = "\"" + Password + "\"";
	    //不广播其SSID的网络
	    apConfig.hiddenSSID = true;
	    apConfig.status = WifiConfiguration.Status.ENABLED;
	    //公认的IEEE 802.11验证算法。
	    apConfig.allowedAuthAlgorithms.clear();
	    apConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	    //公认的的公共组密码
	    apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	    apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	    //公认的密钥管理方案
	    apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	    //密码为WPA。
	    apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	    apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	    //公认的安全协议。
	    apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	    return apConfig;
	  }
	  
	  /**
	   * 连接没有密码wifi.
	   *
	   * @param ssid ssid
	   * @return configuration
	   */
	  private WifiConfiguration setWifiParamsNoPassword(String ssid) {
	    WifiConfiguration configuration = new WifiConfiguration();
	    configuration.SSID = "\"" + ssid + "\"";
	    configuration.status = WifiConfiguration.Status.ENABLED;
	    configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	    configuration.allowedPairwiseCiphers
	        .set(WifiConfiguration.PairwiseCipher.TKIP);
	    configuration.allowedPairwiseCiphers
	        .set(WifiConfiguration.PairwiseCipher.CCMP);
	    configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	    return configuration;
	  }
	  
	  public static final int WIFI_NO_PASS = 0;
	  private static final int WIFI_WEP = 1;
	  private static final int WIFI_PSK = 2;
	  private static final int WIFI_EAP = 3;
	  
	  /**
	   * 判断是否有密码.
	   *
	   * @param result ScanResult
	   * @return 0
	   */
	  public static int getSecurity(ScanResult result) {
	    if (null != result && null != result.capabilities) {
	      if (result.capabilities.contains("WEP")) {
	        return WIFI_WEP;
	      } else if (result.capabilities.contains("PSK")) {
	        return WIFI_PSK;
	      } else if (result.capabilities.contains("EAP")) {
	        return WIFI_EAP;
	      }
	    }
	    return WIFI_NO_PASS;
	  }

	public void enable(WifiManager wifiMgr, String name, String password) {
		if(null != password){
			wifiMgr.enableNetwork(wifiMgr.addNetwork(setWifiParamsPassword(name, password)), true);
		}else{		// 不需要密码
			wifiMgr.enableNetwork(wifiMgr.addNetwork(setWifiParamsNoPassword(name)), true);
		}
	}
}
