package com.yusun.cartracker.model;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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
		String imei = Imei;
		if(null != imei && imei.length() == 15){
			imei = "0" + imei;
		}
		try {
			buf.writeBytes(Hex.decodeHex(Imei.toCharArray()));
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buf.writeShort(Integer.parseInt(DeviceType));
		buf.writeShort(Language);
		return buf;
	}
}
