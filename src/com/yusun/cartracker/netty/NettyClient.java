package com.yusun.cartracker.netty;


import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.protocol.abs.BaseProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	private Channel channel;
    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {                    	
                    	BaseProtocol bs = AppContext.instance().getmProtocolMgr().getmProtocol();
        				bs.onInitChannel(ch);
                    }                    
                });

        channel = bootstrap.connect("127.0.0.1", 7018).channel();
        //while (true) {
        //    channel.writeAndFlush(new Date() + ": hello world!");
        //    Thread.sleep(2000);
        //}
    }
	public void sendMessage(Object msg){
		channel.writeAndFlush(msg);
	}
	public Channel getChannel(){
		return channel;
	}
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}