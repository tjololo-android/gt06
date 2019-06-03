package com.yusun.cartracker.protocol.abs;
public interface IClientStatus {
	void onConnected();
	void onDisconnected();
	void onLogin();
}
