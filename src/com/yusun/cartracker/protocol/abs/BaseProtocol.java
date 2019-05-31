package com.yusun.cartracker.protocol.abs;

import io.netty.channel.socket.SocketChannel;

public abstract class BaseProtocol {
	abstract public void addProtocolHandlers(SocketChannel socketChannel);
}
