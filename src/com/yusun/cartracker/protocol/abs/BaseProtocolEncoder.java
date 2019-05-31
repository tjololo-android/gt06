package com.yusun.cartracker.protocol.abs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class BaseProtocolEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
       ByteBuf buf = encodeContent(msg);
       ctx.writeAndFlush(buf);
    }
    abstract public ByteBuf encodeContent(Object msg);
}
