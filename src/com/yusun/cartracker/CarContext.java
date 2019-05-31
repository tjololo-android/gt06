package com.yusun.cartracker;

import com.yusun.cartracker.model.CmdMgr;
import com.yusun.cartracker.model.NetworkManager.NetworkHandler;
import com.yusun.cartracker.model.TaskMgr;
import com.yusun.cartracker.netty.Client;
import com.yusun.cartracker.protocol.abs.ProtocolMgr;

public class CarContext implements NetworkHandler{
	public void init(){		
		mCmdMgr = new CmdMgr();
		mTaskMgr = new TaskMgr();
		mTaskMgr.init();
		mProtocolMgr = new ProtocolMgr();
		mProtocolMgr.init();
	}
	
	
	private CmdMgr mCmdMgr;
	private TaskMgr mTaskMgr;
	private ProtocolMgr mProtocolMgr;
	
	private CarContext(){
		
	}
	public CmdMgr getmCmdMgr() {
		return mCmdMgr;
	}
	public void setmCmdMgr(CmdMgr mCmdMgr) {
		this.mCmdMgr = mCmdMgr;
	}
	public TaskMgr getmTaskMgr() {
		return mTaskMgr;
	}
	public void setmTaskMgr(TaskMgr mEchoMgr) {
		this.mTaskMgr = mEchoMgr;
	}
	public ProtocolMgr getmProtocolMgr() {
		return mProtocolMgr;
	}
	public void setmProtocolMgr(ProtocolMgr mProtocolMgr) {
		this.mProtocolMgr = mProtocolMgr;
	}
	
	private boolean isOnline = false;
    @Override
    public void onNetworkUpdate(boolean isOnline) {
        if (!this.isOnline && isOnline) {
        }
        this.isOnline = isOnline;
    }
	
	private static CarContext _this;
	public static CarContext instance(){
		if(null == _this){
			_this = new CarContext();			
		}
		return _this;
	}
	
	private Client mClient;
	public void setClient(Client client){
		mClient = client;
	}
	public Client getClient() {
		return mClient;
	}
}