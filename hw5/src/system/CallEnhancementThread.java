package system;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import api.ComposeTask;
import api.Result;
import api.Task;

public class CallEnhancementThread implements Runnable{

	Task<?> task;
	ComputerImpl computerImpl;

	CallEnhancementThread(Task task, ComputerImpl computerImpl){
		this.task = task;
		this.computerImpl = computerImpl;	
	}

	@Override
	public void run() {

		while(true){
			final long taskStartTime = System.nanoTime();
			task.setComputer(computerImpl);
			Object obj = null;
			try {
				obj = task.call();
			} catch (RemoteException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			List<Task<?>> children = task.generateChildren();

			Task prefetchTask = null;
			if(children != null){
				for(int x=0;x<children.size();x++){
					Task xTask = children.get(x);						

					if(!(xTask instanceof ComposeTask)){
						prefetchTask = xTask;
						children.remove(xTask);
						break;
					}

				}
			}

			if(children!= null){
				try {
					computerImpl.sendTaskToSpace(children);
				} catch (RemoteException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

			final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;

			Logger.getLogger( ComputerImpl.class.getCanonicalName() )
			.log( Level.INFO, "Task {0} execution time: {1}", new Object[]{ task, taskRunTime } );
			
			task.addRunTime(taskRunTime);
			Map<String, Object> map = task.sucessorMap(); 
			if(map != null)
				try {
					computerImpl.sendUpdateWaiting((String) map.get("parentId"), map);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			Result result = new Result(obj, taskRunTime, task.getArgs(), task.getId(),task.getTOne(), task.getTInf());

			try {
				computerImpl.sendResultToSpace(result);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			if(prefetchTask == null) break;
			else {				
				task = prefetchTask;
			}

		}
	}

}
