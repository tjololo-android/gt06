package com.yusun.cartracker.protocol;

import com.yusun.cartracker.helper.BitUtil;
import com.yusun.cartracker.model.Message;

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
		status = BitUtil.set(status, electronic_b7, 7);
		status = BitUtil.set(status, gpsfix_b6, 6);
		status = BitUtil.set(status, recharge_b2, 2);
		status = BitUtil.set(status, acc_b1, 1);
		status = BitUtil.set(status, guard_b0, 0);
		buf.writeByte(status);
		buf.writeShort(valtage);
		buf.writeByte(gsmSignal);
		buf.writeShort(language);
		return buf;
	}
}
