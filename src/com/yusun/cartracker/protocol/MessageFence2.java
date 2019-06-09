package com.yusun.cartracker.protocol;

import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessageFence2 extends MessageFence{
	public MessageFence2(int id, Position pos) {
		super(id, pos);
	}	
	
	int fenceIndex;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();	
		buf.writeByte(fenceIndex);
		return buf;
	}
}
