package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import api.*;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class SpaceImpl extends UnicastRemoteObject implements Space{

	BlockingQueue<Task> ready;
	ConcurrentHashMap<String, BlockingQueue<ComposeTask>> waiting;

	BlockingQueue<Result> results;

	private Shared shared;

	String finalResultId;


	List<ComputerProxy> computerProxyList;

	protected SpaceImpl() throws RemoteException {
		super();
		ready = new LinkedBlockingQueue<>();
		waiting = new ConcurrentHashMap<String, BlockingQueue<ComposeTask>>();
		results = new LinkedBlockingQueue<>();
		computerProxyList = new ArrayList();
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

	public void updateWaiting(String id, Map map) throws RemoteException{
		synchronized(waiting){

			if(waiting.containsKey(id) && !waiting.get(id).isEmpty()){
				waiting.get(id).element().updateVariables(map);

				if(waiting.get(id).element().getCount() == 0){
					try {
						ready.add( waiting.get(id).take());


					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void putAll(List<Task<?>> taskList) throws RemoteException {
		for(Task t : taskList){
			putTask(t);
		}
	}

	@Override
	public void putTask(Task task, String finalId) throws RemoteException {
		this.finalResultId = finalId;
		putTask(task);
	}

	@Override
	public void putTask(Task<?> task) throws RemoteException {
		if(task.getCount() == 0){
			ready.add(task);
		}else{
			ComposeTask ctask = (ComposeTask) task;

			if(waiting.containsKey(task.getId())){
				waiting.get(ctask.getId()).add(ctask);
			}else{
				waiting.put(ctask.getId(), new  LinkedBlockingDeque<ComposeTask>());
				waiting.get(ctask.getId()).add(ctask);
			}
		}
	}

	@Override
	public void putResult(Result result) throws RemoteException {	
		if(result.getId().equals(finalResultId)){
			results.add(result);
		}
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
	public void register(Computer computer, int threadsNumber) throws RemoteException {
		ComputerProxy computerProxy = new ComputerProxy(computer, this, threadsNumber);
		computerProxyList.add(computerProxy);
		computerProxy.start();

	}

	@Override
	public Task getTask() throws RemoteException {
		Task task = null;
		try {
			task = ready.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException("getTask() failure");
		}

		return task;
	}

	@Override
	public void putTasks(List<Task<?>> children) throws RemoteException {
		for(Task<?> t : children){
			putTask(t);
		}

	}

	@Override
	public List<Task<?>> getTasks(int tasksNumber) throws RemoteException{
		List<Task<?>> ret = new ArrayList<Task<?>>();

		synchronized(ready){
			tasksNumber = Math.min(tasksNumber, ready.size());
			for(int x=0;x<tasksNumber;x++){
				try {
					ret.add(ready.take());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}

		if(tasksNumber == 0){
			try {
				ret.add(ready.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	@Override
	public void putResults(List<Result<?>> results) throws RemoteException {
		if(results != null){
			for(Result<?> result : results){
				putResult(result);
			}
		}

	}

	@Override
	public void setShared(Shared shared) throws RemoteException {

		if(this.shared == null){
			this.shared = shared;
			
			System.out.println(shared.get() + " SHARED VALUE");
			
			//System.out.println("OI");
			//broadcast();
			return;
		}

		boolean ok = false;

		synchronized(this.shared){


			if(shared.isNewer(this.shared)) {
				this.shared.setShared(shared);
				ok = true;
			}

		}

		if(ok)broadcast();
	}

	@Override
	public void broadcast() throws RemoteException {

		List<Integer> remove = new ArrayList<Integer>();

		for(int i = 0;i < computerProxyList.size(); i++) {
			try{
				computerProxyList.get(i).broadcast(shared);
			}catch(Exception e){
				remove.add(i);
			}
		}

		for(int i : remove){
			computerProxyList.remove(i);
		}

	}

	@Override
	public Shared getShared() throws RemoteException {
		return this.shared;
	}



}
