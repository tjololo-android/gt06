package com.yusun.cartracker.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TaskMgr extends Object{
	private static TaskMgr _this;
	public static TaskMgr instance(){
		return _this;
	}
	
	private final Set<Task> Tasks = new HashSet<Task>();
	public void add(Task t){
		Tasks.add(t);
	}
	public void delete(Task t){
		Tasks.remove(t);
	}
	public void exec(){
		Iterator<Task> it = Tasks.iterator();
		while(it.hasNext()){
			Task t = it.next();
			t.exec();
		}
	}
}
