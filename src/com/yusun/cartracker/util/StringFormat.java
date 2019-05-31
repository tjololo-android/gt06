package com.yusun.cartracker.util;

public class StringFormat {
	public static byte[] decodeStringToBytes(String imei) {	
		byte[] returnValue = new byte[8];
		imei = '0'+imei;
		for (int i = 0; i < 8; i++) {
			returnValue[i] = (byte) (Integer.parseInt(imei.substring(2*i, 2*i+1)) << 4 & 0xf0);
			returnValue[i] |= (byte) (Integer.parseInt(imei.substring(2*i+1, 2*i+2)) & 0x0f);
		}
		return returnValue;
	}
	public static byte setBit(byte data, boolean one, int index){
		if(one)
			data |= (byte)(0xff & 1 << index);
		else
			data &= (byte)(~(1 << index));
		return data;
	}	
}

