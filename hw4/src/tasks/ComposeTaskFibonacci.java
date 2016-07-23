package tasks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.ComposeTask;
import api.Space;
import api.Task;

public class ComposeTaskFibonacci extends ComposeTask<Integer> implements Serializable{

	
	private Integer solution = 0;	
	
	public ComposeTaskFibonacci(int count, String id) {
		super(count, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer call() {
		System.out.println("composeID:"+getId());
		return solution;
	}

	public Map<String, Object> sucessorMap(){
		Map<String, Object> map = new HashMap<String,Object>();
		//map.put("value", value);
		map.put("solution", solution);
		
		String newId = "C";
		String[] ar = getId().split("_");
		
		for(int x=1;x<ar.length -1;x++){
			newId += ("_" + ar[x]);
		}
		
		map.put("parentId", newId );
		
		return map;
	}
	
	@Override
	public synchronized void updateVariables(Map<String, Object> map) {
		
		Integer mapSolution = (Integer) map.get("solution");
		
			solution += mapSolution;
			
		
		decreaseCount();
	}

	@Override
	public List<Task<?>> generateChildren() {
		return null;
	}
	

}
