package com.yusun.cartracker.protocol;

import com.yusun.cartracker.helper.Hex;
import com.yusun.cartracker.model.Message;

import io.netty.buffer.ByteBuf;

public class MessageLogin extends Message{
	public MessageLogin(int id) {
		super(id);
	}
	public String DeviceType;
	public int Language;	
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();	
		buf.writeBytes(Hex.encodeHex(Imei));
		buf.writeShort(Integer.parseInt(DeviceType));
		buf.writeShort(Language);
		return buf;
	}
}
