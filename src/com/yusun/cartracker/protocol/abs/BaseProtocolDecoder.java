package com.yusun.cartracker.protocol.abs;

import java.net.SocketAddress;

import com.yusun.cartracker.CarContext;
import com.yusun.cartracker.model.Command;
import com.yusun.cartracker.model.TaskMgr;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class BaseProtocolDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {    
    	
    }

    protected abstract Object decode(Channel channel, SocketAddress remoteAddress, Object msg) throws Exception;
    
    protected void onReceiveCmd(int cmd, Object content){
    	Command cmder = CarContext.instance().getmCmdMgr().get(cmd); 
    	if(null != cmder){
    		cmder.exec(content);
    	}else{
    		TaskMgr tm = CarContext.instance().getmTaskMgr();
    		tm.onEcho(cmd);
    	}
    }
}
