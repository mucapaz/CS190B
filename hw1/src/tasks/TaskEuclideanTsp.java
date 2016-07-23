package tasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import api.Task;

public class TaskEuclideanTsp implements Serializable, Task<List<Integer>> {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private final double[][] cities;
	
	private ArrayList<Integer> atPath;
	private ArrayList<Integer> solution;
	private double solutionCost = Double.POSITIVE_INFINITY;
	private boolean used[];
	
	
	public TaskEuclideanTsp(double[][] cities) {
		this.cities = cities;
		atPath = new ArrayList<Integer>();
		solution = new ArrayList<Integer>();
		used = new boolean[cities.length];
	}
	
	@Override
	public List<Integer> execute() {
		
		used[0] = true;
		perm(0,0.0);
		used[0] = false;
		
		return solution;
	}

	public void perm(int at, double weight){
		if(atPath.isEmpty()){
			atPath.add(at);
			
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
						perm(next, weight + dist(cities[at], cities[next]));
						used[next] = false;
					}
				}
			}
			
			atPath.remove(atPath.size()-1);
		}else{
			atPath.add(at);
			
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
						perm(next, weight + dist(cities[at], cities[next]));
						used[next] = false;
					}
				}
			}
			atPath.remove(atPath.size()-1);
		}
	}
	
	public double dist(double p1[], double p2[]){
		double x = p1[0] - p2[0];
		double y = p1[1] - p2[1];
		return Math.sqrt(x*x + y*y);
	}
	
}
