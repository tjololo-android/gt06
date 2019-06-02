package com.yusun.cartracker.netty;


import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.protocol.abs.BaseProtocol;
import com.yusun.cartracker.util.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	Logger logger = new Logger(NettyClient.class);	
	private Channel channel;
	private String HOST;
	private int PORT;
	public NettyClient(String host, int port){
		HOST = host;
		PORT = port;
	}
    public void start() {
    	logger.info("start+++");
    	
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                    	logger.info("initChannel");
                    	BaseProtocol bs = AppContext.instance().getmProtocolMgr().getmProtocol();
        				bs.onInitChannel(ch);
                    }                    
                });

        channel = bootstrap.connect(HOST, PORT).channel();
        logger.info("start---channel="+channel);
    }
	public void sendMessage(Object msg){
		logger.info("sendMessage"+msg.toString());
		channel.writeAndFlush(msg);
	}
	public Channel getChannel(){
		return channel;
	}
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}