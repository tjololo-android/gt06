package com.yusun.cartracker.model;

import java.util.concurrent.ConcurrentHashMap;

public class Waiter {
	private ConcurrentHashMap<Integer, MyWaiter> mWaiters = new ConcurrentHashMap<Integer, MyWaiter>();
	
	public boolean wait(int cmd, int timeout){
		MyWaiter waiter = mWaiters.get(cmd); 
		if(null != waiter){
			waiter.cancel();
		}
		waiter = new MyWaiter(cmd, timeout);
		mWaiters.put(cmd, waiter);
		return waiter.waitfor();
	}
	public boolean onEcho(int cmd){
		MyWaiter waiter = mWaiters.get(cmd); 
		if(null == waiter)
			return false;
		
		waiter.onEcho();		
		return true;
	}
	
	
	private static Waiter _this;
	public static Waiter instance(){
		if(null == _this){
			_this = new Waiter();
		}
		return _this;
	}	
	
	final class MyWaiter{
		int cmd;
		int timeout;	
		boolean result = false;
		MyWaiter(int cmd, int timeout){
			this.cmd = cmd;
			this.timeout = timeout;
		}
		
		void onEcho(){
			synchronized (this) {
				result = true;
				notify();
			}			
		}
		void cancel(){
			synchronized (this) {
				notify();
			}			
		}
		boolean waitfor(){
			synchronized (this) {
				try {
					wait(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mWaiters.remove(cmd);
			}
			return result;
		}
	}
}
