package com.yusun.cartracker.protocol;

import java.util.Date;

import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MessageLbs extends CMessage{
	public MessageLbs(int id) {		
		super(id);
		
		d = new Date();
		lbs = new LBS();
		language = Settings.instance().getLanguage();
	}
	public String DeviceType;

	public Date d;
	public LBS lbs;
	public int language;	
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeByte(d.getYear());
		buf.writeByte(d.getMonth());
		buf.writeByte(d.getDay());
		buf.writeByte(d.getHours());
		buf.writeByte(d.getMinutes());
		buf.writeByte(d.getSeconds());
		
		buf.writeShort(Integer.parseInt(lbs.mcc));
		buf.writeByte(Integer.parseInt(lbs.mnc));
		
		ByteBuf tmp = Unpooled.buffer();
		tmp.writeShort(lbs.lac);
		tmp.writeMedium(lbs.cellId);
		tmp.writeByte(lbs.rssi);
		for(int i = 0; i < 7; i++){
			buf.writeBytes(tmp);
		}
		
		buf.writeShort(lbs.preTime);
		buf.writeByte(language);
		
		return buf;
	}
}
