package com.yusun.cartracker.protocol;

import java.util.Date;
import java.util.List;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.api.WifiInfoMgr;
import com.yusun.cartracker.helper.Hex;
import com.yusun.cartracker.model.Message;

import android.net.wifi.ScanResult;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MessageWifi extends Message{
	public MessageWifi(int id) {		
		super(id);
		
		d = new Date();
		mcc = Hardware.instance().getMCC();
		mnc = Hardware.instance().getMNC();
		lac = Hardware.instance().getLAC();
		cellId = Hardware.instance().getCID();
		rssi = Hardware.instance().getSIGNAL();
		preTime = Hardware.instance().getPreTime();
		wifiinfos = WifiInfoMgr.getWifiInfos(Hardware.instance().getContext());
	}
	public String DeviceType;

	public Date d;
	public String mcc;
	public String mnc;
	public int lac;
	public int cellId;
	public int rssi;
	public int preTime;
	public List<ScanResult> wifiinfos;
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		buf.writeByte(d.getYear());
		buf.writeByte(d.getMonth());
		buf.writeByte(d.getDay());
		buf.writeByte(d.getHours());
		buf.writeByte(d.getMinutes());
		buf.writeByte(d.getSeconds());
		
		buf.writeShort(Integer.parseInt(mcc));
		buf.writeByte(Integer.parseInt(mnc));
		
		ByteBuf tmp = Unpooled.buffer();
		tmp.writeShort(lac);
		tmp.writeMedium(cellId);
		tmp.writeByte(rssi);
		for(int i = 0; i < 7; i++){
			buf.writeBytes(tmp);
		}
		
		buf.writeShort(preTime);
		
		int count = 0;
		if(null != wifiinfos){
			count = wifiinfos.size();
		}
		buf.writeByte(count);		
		for(int i = 0; i < count; i++){
			ScanResult sr = wifiinfos.get(i);
			buf.writeByte(sr.level);
			buf.writeBytes(sr.BSSID.getBytes());
		}		
		return buf;
	}
}
