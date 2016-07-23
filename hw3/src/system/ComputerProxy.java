package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import api.Result;
import api.Space;
import api.Task;

import java.util.concurrent.*;

public class ComputerProxy extends Thread{
	
	private final Computer computer;
	private final Space space;
	
	public ComputerProxy(Computer computer, Space space){
		this.computer = computer;
		this.space = space;
	}
	
	public void run(){
		boolean ok = true;
		while(ok){
			try {
				Task task = space.getTask();
			
				try{
					Result result = computer.call(task);
					space.putResult(result);
				}catch(RemoteException e){
					e.printStackTrace();
					space.putTask(task);
					ok = false;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ok = false;
			}
		}
	}
}
