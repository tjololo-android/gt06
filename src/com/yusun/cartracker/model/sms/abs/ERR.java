package com.yusun.cartracker.model.sms.abs;

public final class ERR {
	public static final int ER_LENGTH = 100;        //out of length
	public static final int ER_DATA = 101;          //out of data
	public static final int ER_CODE_FORMAT = 102;   //code format error
	public static final int ER_PASS_FORMAT = 103;   //pass format error
	public static final int ER_SOS_MAX = 104;       //sos number out of system
	public static final int ER_SOS_EMPTY = 105;     //sos number empty
	public static final int ER_SOS_INDEX = 106;     //sos number deleted index error
	public static final int ER_NO_CMD = 107;        //not support this command
	public static final int ER_END = 108;           //not end #
	public static final int ER_PASS = 109;          //invalid password number	
}
