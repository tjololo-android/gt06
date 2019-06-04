package com.yusun.cartracker.model;

import java.util.Date;

import com.yusun.cartracker.helper.BitUtil;
import com.yusun.cartracker.helper.UnitsConverter;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessagePosition extends Message{

	public MessagePosition(int id) {
		super(id);
	}
	
	public Position position;
	public int mcc;
	public int mnc;
	public int lac;
	public int cellId;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		Date d = position.getTime();
		buf.writeByte(d.getYear());
		buf.writeByte(d.getMonth());
		buf.writeByte(d.getDay());
		buf.writeByte(d.getHours());
		buf.writeByte(d.getMinutes());
		buf.writeByte(d.getSeconds());
		
		buf.writeByte(position.getSatellites());		
		
		int latitude = (int)(position.getLatitude() * 60 * 30000);
		buf.writeInt(latitude);		
		int longitude = (int)(position.getLongitude() * 60 * 30000);
		buf.writeInt(longitude);
		buf.writeByte((byte)UnitsConverter.kphFromKnots(position.getSpeed()));
		
		short gpsStatus = (short)(0x01ff & (int)position.getCourse());		//9 bit
		gpsStatus = BitUtil.set(gpsStatus, true, 12);	//fixed
		gpsStatus = BitUtil.set(gpsStatus, true, 10);	//-latitude
		gpsStatus = BitUtil.set(gpsStatus, true, 11);	//-longitude
		buf.writeShort(gpsStatus);  
		
		buf.writeShort(mcc);
		buf.writeByte(mnc);
		buf.writeShort(lac);
		buf.writeMedium(cellId);
		return buf;
	}
}
