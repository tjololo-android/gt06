package com.yusun.cartracker.netty;

import com.yusun.cartracker.CarContext;
import com.yusun.cartracker.protocol.abs.BaseProtocol;
import com.yusun.cartracker.protocol.abs.ProtocolMgr;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client { 
	EventLoopGroup group;
	SocketChannel sc;
	public void start(String host, int port){		
		EventLoopGroup group =new NioEventLoopGroup();
		Bootstrap b=new Bootstrap();
		b.group(group)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() { 
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				BaseProtocol bs = CarContext.instance().getmProtocolMgr().getmProtocol();
				bs.onInitChannel(sc);
				Client.this.sc = sc;
			}			
		});
		try {
			b.connect(host, port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	public void stop(){
		group.shutdownGracefully();
	}
	public void sendMessage(Object msg){
		sc.writeAndFlush(msg);
	}
}