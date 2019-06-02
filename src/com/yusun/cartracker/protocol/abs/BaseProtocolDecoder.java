package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.TaskMgr;
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
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelWritabilityChange");
		super.channelWritabilityChanged(ctx);
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

	protected abstract Object decode(Channel channel,  Object msg) throws Exception;
    
    protected void onReceiveCmd(int cmd, Object content){
    	logger.info("onReceiveCmd");
    	Command cmder = AppContext.instance().getmCmdMgr().get(cmd); 
    	if(null != cmder){
    		cmder.exec(content);
    	}else{
    		TaskMgr tm = AppContext.instance().getmTaskMgr();
    		tm.onEcho(cmd);
    	}
    }
}
