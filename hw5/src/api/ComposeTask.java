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
	public ComposeTask(int count, String id, long parentTOne, long parentTInf) {
		super(count, id, parentTOne, parentTInf);
		// TODO Auto-generated constructor stub
	}
	
	public abstract void  updateVariables(Map<String, Object> map);

}
