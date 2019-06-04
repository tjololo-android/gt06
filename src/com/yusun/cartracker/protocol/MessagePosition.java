package com.yusun.cartracker.protocol;

import java.util.Date;

import com.yusun.cartracker.helper.BitUtil;
import com.yusun.cartracker.helper.UnitsConverter;
import com.yusun.cartracker.model.Message;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;

public class MessagePosition extends Message{

	public MessagePosition(int id) {
		super(id);
	}
	
	public Position position;
	public String mcc;
	public String mnc;
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
		
		short gpsStatus = (short)(0x03ff & (int)position.getCourse());			//10 bit		
		gpsStatus = BitUtil.set(gpsStatus, position.getLatitude() < 0, 10);		//-latitude
		gpsStatus = BitUtil.set(gpsStatus, position.getLongitude() > 0, 11);	//-longitude
		gpsStatus = BitUtil.set(gpsStatus, position.isFixed(), 12);				//fixed
		Date now = new Date();
		boolean realGps = false;
		if(now.getTime() - d.getTime() < 3 * 60 * 1000){
			realGps = true;
		}
		gpsStatus = BitUtil.set(gpsStatus, realGps, 13);						//real time
		buf.writeShort(gpsStatus);  
		
		buf.writeShort(Integer.parseInt(mcc));
		buf.writeByte(Integer.parseInt(mnc));
		buf.writeShort(lac);
		buf.writeMedium(cellId);
		return buf;
	}
}
