package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.util.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class BaseProtocolDecoder extends ChannelInboundHandlerAdapter {
	Logger logger = new Logger(BaseProtocolDecoder.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {    
    	logger.info("channelRead");
    	decode(ctx.channel(), msg);
    }

    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelActive");
    	AppContext.instance().getProtocol().onConnected();
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive");
		AppContext.instance().getProtocol().onDisconnected();
		super.channelInactive(ctx);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("exceptionCaught");
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.info("userEventTriggered");
		super.userEventTriggered(ctx, evt);
	}

	protected abstract Object decode(Channel channel,  Object msg) throws Exception;
    
    protected void onReceiveCmd(int cmd, Object content){
    	logger.info("onReceiveCmd");
    	AppContext.instance().getProtocol().onReceive(cmd, content);
    }
}
