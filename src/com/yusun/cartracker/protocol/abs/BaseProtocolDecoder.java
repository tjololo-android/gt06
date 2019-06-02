package com.yusun.cartracker.protocol.abs;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.TaskMgr;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class BaseProtocolDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {    
    	decode(ctx.channel(), msg);
    }

    protected abstract Object decode(Channel channel,  Object msg) throws Exception;
    
    protected void onReceiveCmd(int cmd, Object content){
    	Command cmder = AppContext.instance().getmCmdMgr().get(cmd); 
    	if(null != cmder){
    		cmder.exec(content);
    	}else{
    		TaskMgr tm = AppContext.instance().getmTaskMgr();
    		tm.onEcho(cmd);
    	}
    }
}
