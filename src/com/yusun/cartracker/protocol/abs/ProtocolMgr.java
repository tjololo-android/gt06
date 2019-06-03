package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.protocol.Gt06Protocol;

public class ProtocolMgr {
	private static BaseProtocol mProtocol;
	public static BaseProtocol getProtocol(){
		if(null == mProtocol){
			mProtocol = new Gt06Protocol();
		}
		return mProtocol;
	}
}
