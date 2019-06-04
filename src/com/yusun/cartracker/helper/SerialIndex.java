package com.yusun.cartracker.helper;

public class SerialIndex {
	private static int index = 0;
	public static int get(){
		return index++;
	}
}
