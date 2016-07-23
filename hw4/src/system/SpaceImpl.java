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

import api.ComposeTask;
import api.Result;
import api.Space;
import api.Task;

import java.util.concurrent.*;

public class SpaceImpl extends UnicastRemoteObject implements Space{

	BlockingQueue<Task> ready;
	ConcurrentHashMap<String, BlockingQueue<ComposeTask>> waiting;

	BlockingQueue<Result> results;

	String finalResultId;

	protected SpaceImpl() throws RemoteException {
		super();
		ready = new LinkedBlockingQueue<>();
		waiting = new ConcurrentHashMap<String, BlockingQueue<ComposeTask>>();
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

	public void updateWaiting(String id, Map map) throws RemoteException{
		synchronized(waiting){
			//System.out.println(id + " " + waiting.containsKey(id) + "  " +  waiting.get(id).isEmpty());

			if(waiting.containsKey(id) && !waiting.get(id).isEmpty()){
				//System.out.println("AT LEAST ONE TIME");
				waiting.get(id).element().updateVariables(map);

				if(waiting.get(id).element().getCount() == 0){
					try {
						//System.out.println("HELLO ");
						ready.add( waiting.get(id).take());

						//System.out.println(id);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				//System.out.println(id);

				//System.out.println(waiting.));

				//System.out.println("SOMETHING WENT VERY WRONG");
			}
		}
	}

	
	//putAll (List<Task<T>> taskList) throws RemoteException
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
				//System.out.println(ctask.getId() + " HEY");

				waiting.put(ctask.getId(), new  LinkedBlockingDeque<ComposeTask>());
				//System.out.println(waiting.containsKey(ctask.getId()));
				waiting.get(ctask.getId()).add(ctask);
				//System.out.println(waiting.contains(ctask.getId()));
			}
		}
	}

	@Override
	public void putResult(Result result) throws RemoteException {
		//System.out.println(result.getId() + " RESULT");		
		if(result.getId().equals(finalResultId)){
			//System.out.println(result.getId() + " RESULT ------------------- ");		
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
	

}
