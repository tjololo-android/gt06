package com.yusun.cartracker.protocol;

import java.util.Date;

import com.yusun.cartracker.helper.BitUtil;
import com.yusun.cartracker.helper.UnitsConverter;
import com.yusun.cartracker.position.Position;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GPS{
	public Position position;
	
	public GPS(Position pos){
		position = pos;
	}

	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer();	
		
		Date d = position.getTime();
		buf.writeByte(d.getYear());
		buf.writeByte(d.getMonth());
		buf.writeByte(d.getDay());
		buf.writeByte(d.getHours());
		buf.writeByte(d.getMinutes());
		buf.writeByte(d.getSeconds());
		
		int satellites = 0xc0 | (0x0f & position.getSatellites());		
		buf.writeByte(satellites);		
		
		double abslatitude = Math.abs(position.getLatitude());
		//long latitude = (long)abslatitude;
		//double decimal = abslatitude - latitude;
		//buf.writeInt((int)(latitude*60 + decimal)*30000);
		buf.writeInt((int)(abslatitude*60*30000));
		
		double abslongitude = Math.abs(position.getLongitude());
		//long longitude = (long)abslongitude;
		//decimal = abslongitude-longitude;
		//buf.writeInt((int)(longitude*60+decimal)*30000);
		buf.writeInt((int)(abslongitude*60*30000));
		
		buf.writeByte((byte)UnitsConverter.kphFromKnots(position.getSpeed()));
		
		short gpsStatus = (short)(0x03ff & (int)position.getCourse());			//10 bit		
		gpsStatus = BitUtil.set(gpsStatus, position.getLatitude() > 0, 10);		//-latitude
		gpsStatus = BitUtil.set(gpsStatus, position.getLongitude() < 0, 11);	//-longitude
		gpsStatus = BitUtil.set(gpsStatus, position.isFixed(), 12);				//fixed
		Date now = new Date();
		boolean realGps = false;
		if(now.getTime() - d.getTime() < 3 * 60 * 1000){
			realGps = true;
		}
		gpsStatus = BitUtil.set(gpsStatus, realGps, 13);						//real time
		buf.writeShort(gpsStatus);  			
		return buf;
	}
}
