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

public class SpaceImpl extends UnicastRemoteObject implements Space{

	BlockingQueue<Task> tasks;
	BlockingQueue<Result> results;
		
	protected SpaceImpl() throws RemoteException {
		super();
		tasks = new LinkedBlockingQueue<>();
		results = new LinkedBlockingQueue<>();
	}

	public static void main(String[] args){
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Space engine = new SpaceImpl();
            Registry registry = LocateRegistry.createRegistry(Space.PORT);
            registry.rebind(Space.SERVICE_NAME, engine);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
		
	}
	
	@Override
	public void putAll(List<Task> taskList) throws RemoteException {
		for(Task t : taskList){
			tasks.add(t);
		}
		
	}

	@Override
	public void putTask(Task task) throws RemoteException {
		tasks.add(task);
	}
	
	@Override
	public void putResult(Result result) throws RemoteException {
		results.add(result);
	}
	
	@Override
	public Result take() throws RemoteException {
		Result result = null;
		
		try {
			result = results.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException("take() failure");
		}
		
		return result;
	}

	@Override
	public void register(Computer computer) throws RemoteException {
		ComputerProxy computerProxy = new ComputerProxy(computer, this);
		computerProxy.start();
		
	}

	@Override
	public Task getTask() throws RemoteException {
		Task task = null;
		try {
			task = tasks.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException("getTask() failure");
		}
		
		return task;
	}
	
}
