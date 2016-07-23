package api;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import system.Computer;

/**
 *
 * @author Peter Cappello
 * @param <V> the task return type.
 */
public abstract class Task<V> implements Serializable, Callable<V> 
{
	protected Map<String, String> args;
	
	
	private int count;
	private String id;
	
	private long tOne, tInf, maxTSibling;
	private Computer computer;

	
	public Task(int count, String id){
		this.count = count;
		this.id = id;
		this.tInf = 0;
		this.tOne = 0;
		this.maxTSibling = 0;
		computer = null;
	}
	
	public Task(String id){
		this.count = 0;
		this.id = id;
		this.tInf = 0;
		this.tOne = 0;
		this.maxTSibling = 0;	
		computer = null;
	}
	
	public Task(int count, String id, long parentTOne, long parentTInf){
		this.count = count;
		this.id = id;
		this.maxTSibling = 0;
		tOne = parentTOne;
		tInf = parentTInf;
		computer = null;
	}
	
    @Override
	public abstract V call() throws RemoteException; 
    
    public Map<String, String> getArgs(){
    	return args;
    }

	public synchronized int getCount() {
		return count;
	}

	public synchronized void decreaseCount(){
		this.count--;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public abstract Map<String, Object> sucessorMap();
    
	public abstract List<Task<?>> generateChildren();
	
	public void updateTInf(long siblingTInf) {
		if(siblingTInf > maxTSibling)
			maxTSibling = siblingTInf;
	}
	public void updateTOne(long siblingTOne) {
		tOne += siblingTOne;
	}

	public void addRunTime(long taskRunTime) {
		tOne += taskRunTime;
		tInf += taskRunTime;		
	}
	public long getTInf() {
		return tInf + maxTSibling;
	}
	public long getTOne() {
		return tOne;
	}

	public Shared getShared() throws RemoteException {	
		return computer.getShared();			
	}
	
	public void setShared(Shared shared) throws RemoteException {
		computer.setShared(shared);
	}
	
	public void setComputer(Computer computer) { 
		this.computer = computer;
	};
	
}