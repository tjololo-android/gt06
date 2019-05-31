package com.yusun.cartracker.model;

import com.yusun.cartracker.util.StringFormat;

import io.netty.buffer.ByteBuf;

public class MessageHeartbeat extends Message{

	public MessageHeartbeat(int id) {
		super(id);
	}
	
	public boolean electronic_b7;
	public boolean gpsfix_b6;
	public boolean recharge_b2;
	public boolean acc_b1;
	public boolean guard_b0;
	public int valtage;
	public int gsmSignal;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		byte status = 0;
		status = StringFormat.setBit(status, electronic_b7, 7);
		status = StringFormat.setBit(status, gpsfix_b6, 6);
		status = StringFormat.setBit(status, recharge_b2, 2);
		status = StringFormat.setBit(status, acc_b1, 1);
		status = StringFormat.setBit(status, guard_b0, 0);
		buf.writeByte(status);
		buf.writeShort(valtage);
		buf.writeByte(gsmSignal);
		buf.writeShort(language);
		return buf;
	}
}
