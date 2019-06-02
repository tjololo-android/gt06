package com.yusun.cartracker.protocol.abs;

import io.netty.channel.Channel;

public abstract class BaseProtocol {
	abstract public void onInitChannel(Channel ch);
	abstract public void init();
	abstract public void start();
	abstract public void stop();
}
