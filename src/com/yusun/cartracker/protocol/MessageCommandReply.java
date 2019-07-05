package com.yusun.cartracker.protocol;

import java.io.UnsupportedEncodingException;

import com.yusun.cartracker.model.CMessage;

import io.netty.buffer.ByteBuf;

public class MessageCommandReply extends CMessage{

	public MessageCommandReply(String content, int serverFlag) {
		super(0x21);
		this.serverFlag = serverFlag;
		this.content = content;
	}
	
	private int serverFlag;
	private int code = 0x01;
	private String content;
	public void setCode(int code) {
		this.code = code;
	}
	
	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();
		if(null != content)
			try {
				buf.writeInt(serverFlag);
				buf.writeByte(code);
				String codec = this.code == 1 ? "ascii" : "utf16-be";
				buf.writeBytes(content.getBytes(codec));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return buf;
	}
}
