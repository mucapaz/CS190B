package system;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import api.Result;
import api.Shared;
import api.Space;
import api.Task;

import java.util.concurrent.*;

public class ComputerProxy<T> extends Thread implements Serializable{

	private final Computer computer;
	private final Space space;
	private Result result;
	private int processors;
	List<Task<?>> tasks;
	Task task;


	public ComputerProxy(Computer computer, Space space, int processors){
		this.computer = computer;
		this.space = space;
		this.result = null;
		try {
			this.computer.registerComputerProxy(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.processors = processors;
		this.tasks = new ArrayList<Task<?>>();
	}

	@SuppressWarnings("unchecked")
	public void run(){
		boolean ok = true;
		while(ok){
			try {
				if(processors == 1)
					tasks.add(space.getTask());
				else
					tasks = space.getTasks(processors);

				if(tasks != null && tasks.size() >0){
					try{
						//Result result = computer.call(task);
						List<Result<?>> results = computer.call(tasks);
						space.putResults(results);
					}catch(RemoteException e){
						e.printStackTrace();					
						space.putTasks(tasks);					
						ok = false;
					}
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ok = false;
			}
			
			tasks.clear();
		}
	}

	public void putResult(Result result){
		this.result = result;
	}

	public void sendResultToSpace(Result result) throws RemoteException{
		space.putResult(result);
	}

	public synchronized void setPrefetchTask(Task previoustask,Task prefetchTask){
		tasks.remove(previoustask);
		tasks.add(prefetchTask);		
	}
	
	public void broadcast(Shared shared) throws RemoteException { // Space calls computerProxy.broadcast to send to computer
		computer.setShared(shared);
	}

	public void setShared(Shared shared) throws RemoteException { // Computer calls computerProxy.setShared to send to computer 
		space.setShared(shared);
	}

}
