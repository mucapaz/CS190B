package tasks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.ComposeTask;
import api.Space;
import api.Task;

public class ComposeTaskEuclideanTsp extends ComposeTask<List<Integer>> implements Serializable{

	private List<Integer> path;
	private double cost = Double.MAX_VALUE;	
	
	public ComposeTaskEuclideanTsp(int count, String id) {
		super(count, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Integer> call() {
		return path;
	}

	public Map<String, Object> sucessorMap(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("path", path);
		map.put("cost", cost);
		
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
		
		double mapCost = (Double) map.get("cost");
		if(mapCost < cost){
			path = (List<Integer>) map.get("path");
			cost = mapCost;
			
		}
		decreaseCount();
	}

	@Override
	public List<Task<?>> generateChildren() {
		return null;
	}
	

}
