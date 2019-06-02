package com.yusun.cartracker.protocol.abs;

import java.net.SocketAddress;

import com.yusun.cartracker.util.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class BaseProtocolEncoder extends ChannelOutboundHandlerAdapter {
	Logger logger = new Logger(BaseProtocolEncoder.class);
	
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
       logger.info("write");
       ByteBuf buf = encodeContent(msg);
       ctx.writeAndFlush(buf);
    }
    abstract public ByteBuf encodeContent(Object msg);
	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		logger.info("bind");
		super.bind(ctx, localAddress, promise);
	}
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.info("close");
		super.close(ctx, promise);
	}
	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		logger.info("connect");
		super.connect(ctx, remoteAddress, localAddress, promise);
	}
	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.info("deregister");
		super.deregister(ctx, promise);
	}
	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		logger.info("disconnect");
		super.disconnect(ctx, promise);
	}
	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.info("flush");
		super.flush(ctx);
	}
	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		logger.info("read");
		super.read(ctx);
	}
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.info("handlerAdded");
		super.handlerAdded(ctx);
	}
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("handlerRemoved");
		super.handlerRemoved(ctx);
	}
    
    
}
