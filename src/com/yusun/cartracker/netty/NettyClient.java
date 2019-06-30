package com.yusun.cartracker.netty;

import com.yusun.cartracker.AppContext;
import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	Logger logger = new Logger(NettyClient.class);
	private Channel channel;
	private NioEventLoopGroup group;	
	private String HOST;
	private int PORT;
	public NettyClient(){		
	}
    public void start() {
    	logger.info("start+++");
    	
    	HOST = Hardware.instance().getIp();
    	PORT = Integer.parseInt(Hardware.instance().getPort());
    	
    	Bootstrap bootstrap = new Bootstrap();
    	group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                    	logger.info("initChannel");
                    	AppContext.instance().getProtocol().onInitChannel(ch);
                    }                    
                });

        try {
			ChannelFuture cf = bootstrap.connect(HOST, PORT).sync();
			if(cf.isSuccess()){
				channel = cf.channel();
			}else{
				logger.info("fail connect to server!");
			}
		} catch (InterruptedException e) {			
			logger.error("start---channel=",e);
		} catch (Exception e){			
			logger.error("start---channel=",e);
		}
        logger.info("start---channel="+channel);
    }
	public void sendMessage(Object msg){		
		if(null != channel){
			logger.info("sendMessage+++");
			channel.writeAndFlush(msg);
		}else{
			logger.info("sendMessage error channel=null");			
		}
	}
	public void stop() {
		logger.info("stop shutdownGracefully");
		if(null != channel){
			channel.closeFuture();
			group.shutdownGracefully();
			channel = null;
		}
	}
}