package com.yusun.cartracker.protocol;

import com.yusun.cartracker.helper.Logger;
import com.yusun.cartracker.model.CMessage;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessagePosition extends CMessage{
	Logger logger = new Logger(MessagePosition.class);

	public MessagePosition(int id, Position pos) {
		super(id);
		gps = new GPS(pos);
		lbs = new LBS();
	}	
	
	GPS gps;
	LBS lbs;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeBytes(gps.encode());
		
		buf.writeShort(Integer.parseInt(lbs.mcc));
		buf.writeByte(Integer.parseInt(lbs.mnc));
		buf.writeShort(lbs.lac);
		buf.writeMedium(lbs.cellId);
		
		return buf;
	}
}
