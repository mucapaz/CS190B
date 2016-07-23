package api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
	
	public Task(int count, String id){
		this.count = count;
		this.id = id;
	}
	
	public Task(String id){
		this.count = 0;
		this.id = id;
	}
	
    @Override
	public abstract V call(); 
    
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
	
}