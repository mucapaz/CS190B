package api;

import java.util.Map;

public abstract class ComposeTask<V>  extends Task<V> {

	public ComposeTask(int count, String id) {
		super(count, id);
		// TODO Auto-generated constructor stub
	}
	public ComposeTask(String id){
		super(id);
	}
	
	public abstract void  updateVariables(Map<String, Object> map);

}
