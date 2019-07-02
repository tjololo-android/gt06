package com.yusun.cartracker.protocol;

import com.yusun.cartracker.model.Command;

public class Gt06CommandFactory {
	public Command newCommand(int cmd){
		Command command = null;
		switch(cmd){
		case 0x17:			
			break;		
		}
		return command;
	}
}

