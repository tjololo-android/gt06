package com.yusun.cartracker.protocol.abs;

import io.netty.channel.Channel;

public interface IProtocol{
	void onInitChannel(Channel ch);
	void init();
	void uninit();
	void login();
	void start();
	void stop();
	void onEcho(int cmd, Object content);
}
