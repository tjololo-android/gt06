package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.BitUtil;
import com.yusun.cartracker.model.Message;

import io.netty.buffer.ByteBuf;

public class MessageHeartbeat extends Message{

	public MessageHeartbeat(int id) {
		super(id);
		deviceStatus = new DeviceStatus();		
		valtage = Hardware.instance().getBATTERY();
		gsmSignal = Hardware.instance().getSIGNAL();
		language = Hardware.instance().getLanguage();
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
