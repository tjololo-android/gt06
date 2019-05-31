package com.yusun.cartracker.protocol.abs;

import java.net.SocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class BaseProtocolDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {     

    }

    protected abstract Object decode(Channel channel, SocketAddress remoteAddress, Object msg) throws Exception;
}
