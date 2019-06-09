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
	   * �����������wifi.
	   *
	   * @param SSID     ssid
	   * @param Password Password
	   * @return apConfig
	   */
	  private WifiConfiguration setWifiParamsPassword(String SSID, String Password) {
	    WifiConfiguration apConfig = new WifiConfiguration();
	    apConfig.SSID = "\"" + SSID + "\"";
	    apConfig.preSharedKey = "\"" + Password + "\"";
	    //���㲥��SSID������
	    apConfig.hiddenSSID = true;
	    apConfig.status = WifiConfiguration.Status.ENABLED;
	    //���ϵ�IEEE 802.11��֤�㷨��
	    apConfig.allowedAuthAlgorithms.clear();
	    apConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
	    //���ϵĵĹ���������
	    apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	    apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	    //���ϵ���Կ������
	    apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	    //����ΪWPA��
	    apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	    apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	    //���ϵİ�ȫЭ�顣
	    apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
	    return apConfig;
	  }
	  
	  /**
	   * ����û������wifi.
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
	   * �ж��Ƿ�������.
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
		}else{		// ����Ҫ����
			wifiMgr.enableNetwork(wifiMgr.addNetwork(setWifiParamsNoPassword(name)), true);
		}
	}
}
