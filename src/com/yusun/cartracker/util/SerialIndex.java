package com.yusun.cartracker.util;

public class SerialIndex {
	private static int index = 0;
	public static int get(){
		return index++;
	}
}
