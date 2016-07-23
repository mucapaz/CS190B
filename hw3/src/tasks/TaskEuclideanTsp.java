package tasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Task;

public class TaskEuclideanTsp extends Task<List<Integer>> implements Serializable{


	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 3;
	private final double[][] cities;

	private ArrayList<Integer> atPath;
	private ArrayList<Integer> solution;
	private double solutionCost = Double.POSITIVE_INFINITY;
	private boolean used[];
	private int secondCity;

	public TaskEuclideanTsp(double[][] cities, ArrayList<Integer> pathTaken, int count, String id) {
		super(count,id);
		this.cities = cities;
		
		atPath = pathTaken;
		solution = new ArrayList<Integer>();
		used = new boolean[cities.length];
		
		for(Integer i : atPath){
			used[i] = true;
		}
		
	}

	@Override
	public List<Integer> call() {
		solution.addAll(atPath);
		if(atPath.size() > LIMIT) {
			double cost = 0.0;
			for(int x=0;x<atPath.size()-1;x++){
				cost += dist(cities[atPath.get(x)], cities[atPath.get(x+1)] ); 
			}
			
			perm(atPath.get(atPath.size()-1),cost);
		}
		return solution;
	}

	public void perm(int at, double weight){
		if(atPath.isEmpty()){
			

			if(atPath.size() == used.length){
				if(solution.isEmpty()){
					solution.add(at);
				}else if(weight < solutionCost){
					solution.add(at);
				}
			}else{
				for(int next=0;next<used.length;next++){
					if(!used[next]){
						used[next] = true;
						atPath.add(next);
						perm(next, weight + dist(cities[at], cities[next]));
						atPath.remove(atPath.size()-1);
						used[next] = false;
					}
				}
			}

			atPath.remove(atPath.size()-1);
		}else{

			if(atPath.size() == used.length){

				weight += dist(cities[atPath.get(0)], cities[at]);

				if(solution.isEmpty()){
					solution.addAll(atPath);
				}else if(weight < solutionCost){
					solution.clear();
					solution.addAll(atPath);
					solutionCost = weight;
				}
			}else{
				for(int next=0;next<used.length;next++){
					if(!used[next]){
						used[next] = true;
						atPath.add(next);
						perm(next, weight + dist(cities[at], cities[next]));
						atPath.remove(atPath.size()-1);
						used[next] = false;
					}
				}
			}
		}
	}

	public double dist(double p1[], double p2[]){
		double x = p1[0] - p2[0];
		double y = p1[1] - p2[1];
		return Math.sqrt(x*x + y*y);
	}

	public double[][] getCities(){
		return this.cities;
	}
	
	@Override
	public Map<String, Object> sucessorMap() {
		if(atPath.size()<=LIMIT) return null;
		
		Map<String, Object> rmap = new HashMap<String,Object>();
		
		rmap.put("path", solution);
		rmap.put("cost", solutionCost);
		
		String newId = "C";
		String[] ar = getId().split("_");
		
		for(int x=1;x<ar.length -1;x++){
			newId += ("_" + ar[x]);
		}
		
		rmap.put("parentId", newId);
		
		return rmap;		
	}

	@Override
	public List<Task> generateChildren() {
		
		if(atPath.size() > LIMIT) return null;
		
		ArrayList<Task> list = new ArrayList<Task>();
		int count = 0;
		if(atPath.size() <=LIMIT ){
			for(int x=0;x<used.length;x++){
				if(!used[x]){
					count++;
					String id = "";
					
					for(Integer k : atPath){
						id+= "_" + k;
					}
					id += "_" + x;
					
					ArrayList<Integer> arn = ((ArrayList<Integer>) atPath.clone());
					arn.add(x);
					
					list.add(new TaskEuclideanTsp(cities, arn,0, id));					
				}
			}
		}
		
		String id = "C";
		for(Integer i : atPath){
			id += "_" + i;
		}
		
		list.add(new ComposeTaskEuclideanTsp(count,id));
		
		return list;
	}	

}





