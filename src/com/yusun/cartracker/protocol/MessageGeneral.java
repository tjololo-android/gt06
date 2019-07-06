package com.yusun.cartracker.protocol;

import java.nio.charset.Charset;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.Settings;
import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class MessageGeneral extends CMessage{
	//ID == 0x7979
	public MessageGeneral() {		
		super(0x94);
	}
	
	int subId;	
	
	public void setSubId(int subId) {
		this.subId = subId;
	}

	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeByte(subId);
		if(0x00 == subId){			
			int battery = Hardware.instance().getBATTERY();
			buf.writeShort(battery);
		}else if(0x04 == subId){
			//ALM1=FF; ALM2=FF; ALM3=FF; STA1=CO £» DYD=01£» SOS=12345£¬ 2345£¬ 5678£» CENTER=987654;
			//FENCE= FENCE,ON,0,-22.277120,-113.516763,5,IN,1£» MODE= MODE,1,20,500
			byte alm1 = 0x1;	//NG
			byte alm2 = 0x1;
			byte alm3 = 0x1;
			byte sta1 = 0x1;
			byte dyd = 0x01;	//NG
			String sos = Settings.instance().getSOS();
			String center = Hardware.instance().getCenterNum();
			String fence = Settings.instance().getFence().toString();
			String mode = Hardware.instance().getMode();
			StringBuilder sb = new StringBuilder();
			sb.append("ALM1="+String.format( "%02x;", alm1));
			sb.append("ALM2="+String.format( "%02x;", alm2));
			sb.append("ALM3="+String.format( "%02x;", alm3));
			sb.append("STA1="+String.format( "%02x;", sta1));
			sb.append("DYD="+String.format( "%02x;", dyd));
			sb.append("SOS="+sos+";");
			sb.append("CENTER="+center+";");
			sb.append("FENCE="+fence+";");
			sb.append("MODE="+mode);
			buf.writeBytes(sb.toString().getBytes(Charset.forName("ascii")));
		}else if(0x05 == subId){
			byte io = 0x1;
			buf.writeByte(io);			
		}else if(0x06 == subId){
			String str = "check self";
			buf.writeBytes(str.getBytes(Charset.forName("ascii")));
		}
		return buf;
	}
}
