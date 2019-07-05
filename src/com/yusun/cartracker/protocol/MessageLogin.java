package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.helper.Hex;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class MessageLogin extends CMessage{
	public MessageLogin() {
		super(0x01);
		DeviceType = Settings.instance().getDeviceType();
		Language = Settings.instance().getLanguage();
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
