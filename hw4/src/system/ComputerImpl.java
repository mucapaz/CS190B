package system;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import api.ComposeTask;
import api.Result;
import api.Space;
import api.Task;

public class ComputerImpl extends UnicastRemoteObject implements Computer{

	private final boolean ENHANCEMENT;
	private Space space;
	private ComputerProxy computerProxy;
	private Task task;

	public ComputerImpl(Space space, boolean ENHANCEMENT, int processors) throws RemoteException{
		super();
		this.ENHANCEMENT = ENHANCEMENT;
		this.space = space;
		task = null;
		space.register(this, processors);
	}

	public static void main(String[] args) {
		System.setSecurityManager( new SecurityManager() );
		String url = "rmi://" + args[0] + ":" + Space.PORT + "/" + Space.SERVICE_NAME;

		String MODE = args[1];
		String THREAD_MODE = args[2];
		ComputerImpl computerImpl;
		Space space;
		System.out.println(MODE + "  "  + THREAD_MODE );
		try {
			space = (Space) Naming.lookup( url );
			
			if(MODE.equals("ON")){
				Runtime runtime = Runtime.getRuntime();
				int processors = runtime.availableProcessors();
				
				
				if(THREAD_MODE.equals("SINGLE")){
					computerImpl = new ComputerImpl(space, true, 1);
				}else{
					computerImpl = new ComputerImpl(space, true, processors);
				}
				//				for(int x=0;x<processors;x++){
				//					new ComputerImpl(space, true, processors);
				//				}

			}else{
				computerImpl = new ComputerImpl(space, false, 1);
			}

			/// 
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// RESULT 1 / RESULT2 / RESULT 3 
	@Override
	public List<Result<?>> call(List<Task<?>> t) throws RemoteException {

		if(ENHANCEMENT){

			List<Thread> threadList = new ArrayList<Thread>();
			for(int x=0;x<t.size();x++){
				threadList.add(new Thread(new CallEnhancementThread(t.get(x), this)));
				threadList.get(x).start();
			}
			
			for(int x=0;x<t.size();x++){
				try {
					threadList.get(x).join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return new ArrayList<Result<?>>();
			
		}else{
			final long taskStartTime = System.nanoTime();
			Task<?> task = t.get(0);
			Object obj = task.call();

			List<Task<?>> children = task.generateChildren();

			if(children!= null)space.putTasks(children);

			final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;

			//System.out.println("FINISHED");
			Logger.getLogger( ComputerImpl.class.getCanonicalName() )
			.log( Level.INFO, "Task {0} execution time: {1}", new Object[]{ t, taskRunTime } );


			Map<String, Object> map = task.sucessorMap();        
			if(map != null)space.updateWaiting((String) map.get("parentId"), map);
			List<Result<?>> results = new ArrayList<Result<?>>();
			results.add(new Result(obj, taskRunTime, task.getArgs(), task.getId()));

			return results;
		}


	}
	
	@Override
	public void registerComputerProxy(ComputerProxy computerProxy) throws RemoteException{
		this.computerProxy = computerProxy;	
	}

	@Override
	public void putTask(Task task) throws RemoteException{
		this.task = task;

	}

	public void sendTaskToSpace(List<Task<?>> tasks) throws RemoteException {
		space.putTasks(tasks);		
	}

	public void sendUpdateWaiting(String string, Map<String, Object> map) throws RemoteException {
		space.updateWaiting(string, map);

	}


	public void sendProxyPrefetchTask(Task previousTask, Task prefetchTask) {
		computerProxy.setPrefetchTask(previousTask, prefetchTask);
		
	}

	public void sendResultToSpace(Result result) throws RemoteException {
		space.putResult(result);
		
	}

}
