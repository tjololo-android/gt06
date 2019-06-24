package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.helper.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class BaseProtocolEncoder extends ChannelOutboundHandlerAdapter {
	Logger logger = new Logger(BaseProtocolEncoder.class);	
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {       
       ByteBuf buf = encodeContent(msg);
       logger.info("send data: "+ByteBufUtil.hexDump((ByteBuf) msg));
       ctx.writeAndFlush(buf);
    }
    abstract public ByteBuf encodeContent(Object msg);
}
