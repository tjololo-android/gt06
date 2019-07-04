package com.yusun.cartracker.protocol;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.helper.BitUtil;

public class DeviceStatus{
	public boolean electronic_b7;
	public boolean gpsfix_b6;
	public boolean recharge_b2;
	public boolean acc_b1;
	public boolean guard_b0;
	public int alarm_type_b3_5;
	
	DeviceStatus(){
		electronic_b7 = Hardware.instance().getOilPowerControl();
		gpsfix_b6 = Hardware.instance().getGpsFixed();
		recharge_b2 = Hardware.instance().getRecharge();
		acc_b1 = Hardware.instance().getAcc();
		guard_b0 = Hardware.instance().getVibration();
		alarm_type_b3_5 = Hardware.instance().getAlarmType();
	}

	public byte encode() {
		byte status = 0;			
		status = BitUtil.set(status, electronic_b7, 7);
		status = BitUtil.set(status, gpsfix_b6, 6);
		status = BitUtil.set(status, recharge_b2, 2);
		status = BitUtil.set(status, acc_b1, 1);
		status = BitUtil.set(status, guard_b0, 0);
		status |= alarm_type_b3_5 << 3 & 0x38;
		return status;
	}
}
