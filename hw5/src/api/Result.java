package api;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Peter Cappello
 * @param <T> type of return value of corresponding Task.
 */
public class Result<T> implements Serializable
{
    private final T taskReturnValue;
    private final long taskRunTime, tOne, tInf;
    private final Map<String, String> map;
    
    private String id; 
    
    public Result( T taskReturnValue, long taskRunTime, Map<String,String> map, String id, long tOne, long tInf)
    {
    	this.map = map;
        assert taskReturnValue != null;
        assert taskRunTime >= 0;
        this.taskReturnValue = taskReturnValue;
        this.taskRunTime = taskRunTime;
        this.id = id;
        this.tOne = tOne;
        this.tInf = tInf;
    }

    public T getTaskReturnValue() { return taskReturnValue; }

    public long getTaskRunTime() { return taskRunTime; }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( "\n\tExecution time:\n\t" ).append( taskRunTime );
        stringBuilder.append( "\n\tReturn value:\n\t" ).append( taskReturnValue.toString() );
        return stringBuilder.toString();
    }
   
    public String mapValue(String name){
    	return map.get(name);
    }

	public String getId() {
		return this.id;
	}
	public long getTOne() {
		return this.tOne;
	}
	public long getTInf() {
		return this.tInf;
	}

	public synchronized void setId(String id) {
		this.id = id;
	}
	
	

}
