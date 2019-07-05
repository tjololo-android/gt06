package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class MessageHeartbeat extends CMessage{

	public MessageHeartbeat() {
		super(0x23);
		deviceStatus = new DeviceStatus();		
		valtage = Hardware.instance().getBATTERY();
		gsmSignal = Hardware.instance().getSIGNAL();
		language = Settings.instance().getLanguage();
	}
	
	DeviceStatus deviceStatus;
	public int valtage;
	public int gsmSignal;
	public int language;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		byte status = deviceStatus.encode();		
		buf.writeByte(status);
		buf.writeShort(valtage);
		buf.writeByte(gsmSignal);
		buf.writeShort(language);
		return buf;
	}
}
