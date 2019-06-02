package com.yusun.cartracker.netty;

import com.yusun.cartracker.protocol.Gt06FrameDecoder;
import com.yusun.cartracker.util.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
	Logger logger = new Logger(NettyServer.class);
    public void start() {
    	logger.info("start+++");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boos, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                    	logger.info("initChannel");
                    	ch.pipeline().addLast(new Gt06FrameDecoder());                        
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								logger.info("receive from client "+ByteBufUtil.hexDump((ByteBuf) msg));
								ctx.writeAndFlush(msg);
							}                            
                        });
                    }
                })
                .bind(7018);
        logger.info("start---");
    }
}