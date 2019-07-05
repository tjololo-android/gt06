package com.yusun.cartracker.protocol;

import java.util.Date;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.DateBuilder;
import com.yusun.cartracker.model.Command;

import io.netty.buffer.ByteBuf;

public class CommandSyncTime extends Command {
	public CommandSyncTime() {
		super(0x8A);
	}
	Date date;
	public void decode(ByteBuf buf) {
		buf.skipBytes(4);
		DateBuilder dateBuilder = new DateBuilder()
				.setDate(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte())
				.setTime(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
		date = dateBuilder.getDate();
	}

	@Override
	public void doCmd() {
		Hardware.instance().setDate(date);
	}
}