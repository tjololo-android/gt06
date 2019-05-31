package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.protocol.Gt06Protocol;

public class ProtocolMgr {
	BaseProtocol mProtocol;
	public BaseProtocol getmProtocol() {
		return mProtocol;
	}
	public void init(){
		mProtocol = new Gt06Protocol(); 
		mProtocol.init();
	}
}
