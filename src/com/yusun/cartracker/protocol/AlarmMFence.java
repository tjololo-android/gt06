package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class AlarmMFence extends AlarmSFence{
	public AlarmMFence(int id, Position pos) {
		super(id, pos);		
		fenceNum = Hardware.instance().getFenceNum();
	}
	int fenceNum;	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeByte(fenceNum);
		return buf;
	}
}
