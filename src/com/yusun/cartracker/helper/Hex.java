package com.yusun.cartracker.helper;

public class Hex {
	public static byte[] encodeHex(String data) {
		int len = data.length();
		if ((len & 0x1) != 0) {
			data = "0" + data;
			++len;
		}

		byte[] out = new byte[len >> 1];

		int i = 0;
		for (int j = 0; j < len; ++i) {
			int f = toDigit(data, j) << 4;
			++j;
			f |= toDigit(data, j);
			++j;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}
	
	public static int toDigit(String s, int index){
		int ret = 0;
		try{
		ret = Integer.parseInt(s.substring(index, index+1));
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return ret;
	}
}