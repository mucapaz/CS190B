package api;

import java.io.Serializable;
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
	
    @Override
	public abstract V call(); 
    
    public Map<String, String> getArgs(){
    	return args;
    }
}