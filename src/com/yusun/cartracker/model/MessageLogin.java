package com.yusun.cartracker.model;

import com.yusun.cartracker.util.StringFormat;

import io.netty.buffer.ByteBuf;

public class MessageLogin extends Message{
	public MessageLogin(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public String DeviceType;
	public int Language;	
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeBytes(StringFormat.decodeStringToBytes(Imei));
		buf.writeShort(Integer.parseInt(DeviceType));
		buf.writeShort(Language);
		return buf;
	}
}
