package com.yusun.cartracker.model.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yusun.cartracker.api.Hardware;
import com.yusun.cartracker.model.sms.abs.CMDS;
import com.yusun.cartracker.model.sms.abs.CmdHandler;
import com.yusun.cartracker.model.sms.abs.SMS;

public class SOS implements CmdHandler{

	@Override
	public String getCmd() {		
		return CMDS.SOS;
	}

	@Override
	public void doCmd(SMS msg) {	
		//SOS,666666,D,1,3,4#
		//SOS,666666,D,13810001000#		
		//SOS,666666,A,13810001000,1390000,,112#
		String r1 = "D,([1-4]),*([1-4])*,*([1-4])*,*([1-4])*";
		String r2 = "D,(\\d+),*(\\d+)*,*(\\d+)*,*(\\d+)*";
		String r3 = "A,(\\d+),*(\\d+)*,*(\\d+)*,*(\\d+)*";
		Matcher m1 = Pattern.compile(r1).matcher(msg.content);
		Matcher m2 = Pattern.compile(r2).matcher(msg.content);
		Matcher m3 = Pattern.compile(r3).matcher(msg.content);
		String[] nums = Hardware.instance().getSOS().split(",");
		if(m1.find()){			//delete by index
			Matcher m = m3;
			for(int i = 1; i < 5; i++){
				String s = m.group(i);
				if(null != s && !s.isEmpty()){
					int index = Integer.parseInt(s);
					nums[index] = "";
				}
			}			
		}else if(m2.find()){	//delete by number
			Matcher m = m2;
			for(int i = 1; i < 5; i++){
				String s = m.group(i);
				if(null != s && !s.isEmpty()){
					boolean found = false;
					for(int j = 0; j < nums.length; j++){
						if(s.equals(nums[j])){
							nums[j] = "";
							found = true;
							break;
						}
					}
					if(!found){
						msg.sendAck("not found num " + s);
						return;
					}
				}
			}			
		}else if(m3.find()){	//add number
			Matcher m = m3;
			for(int i = 1; i < 5; i++){
				String s = m.group(i);
				if(null != s && !s.isEmpty()){					
					nums[i-1] = s;
				}
			}		
		}else{
			msg.sendFormatErr();
			return;
		}
		String s = nums[0] + ',' + nums[1] + ',' +nums[2] + ',' + nums[3];
		Hardware.instance().setSOS(s);
	}	
}
