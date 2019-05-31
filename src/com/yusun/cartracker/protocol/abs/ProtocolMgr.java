package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.protocol.Gt06Protocol;

public class ProtocolMgr {
	private static ProtocolMgr _this;
	public static ProtocolMgr instance(){
		return _this;
	}
	public BaseProtocol getProtocol(String name){
		return new Gt06Protocol();
	}
}
