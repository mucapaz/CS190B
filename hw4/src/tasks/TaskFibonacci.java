package tasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Task;

public class TaskFibonacci extends Task<Integer> implements Serializable{


	private static final long serialVersionUID = 1L;	
	private final int value;
	private Integer solution;


	public TaskFibonacci(int value, int count, String id) {
		super(count,id);
		this.value = value;
		solution = 1;
	}

	@Override
	public Integer call() {
		if(value == 1 || value == 2){
			solution = 1;
		}else if(value == 0){
			solution = 0;
		}
		return solution;
	}	

	@Override
	public Map<String, Object> sucessorMap() {
		if(value < 3){
			Map<String, Object> rmap = new HashMap<String,Object>();
			rmap.put("solution", 1);
			String newId = "C";
			String[] ar = getId().split("_");

			for(int x=0;x<ar.length -1;x++){
				newId += ("_" + ar[x]);
			}

			rmap.put("parentId", newId);

			return rmap;
		}else 
			return null;
		
	}

	@Override
	public List<Task<?>> generateChildren() {

		if(getId().startsWith("C")) return null;

		ArrayList<Task<?>> list = new ArrayList<Task<?>>();
		if(getId().endsWith("_2") || getId().endsWith("_1"))
			return null;
		int count = 0;


		list.add(new TaskFibonacci(value - 1,0, getId() + "_" + (value - 1)));
		list.add(new TaskFibonacci(value - 2,0, getId() + "_" + (value - 2)));


		String id = "C";

		id += "_" + getId();
		//count = 2;

		list.add(new ComposeTaskFibonacci(2,id));

		return list;
	}	

}





