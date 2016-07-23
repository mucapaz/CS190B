package tasks;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Task;
import system.DoubleShared;

public class TaskEuclideanTsp extends Task<List<Integer>> implements Serializable{


	private static final long serialVersionUID = 1L;
	private static final int LIMIT = 3;
	private final double[][] cities;

	private ArrayList<Integer> atPath;
	private ArrayList<Integer> solution;
	private double solutionCost = Double.POSITIVE_INFINITY;	
	private boolean used[];

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

	public static void main(String[] args){
		double[][] cidades =

			{
					{ 1, 1 },
					{ 8, 1 },
					{ 8, 8 },
					{ 1, 8 },
					
					{ 2, 2 },
					{ 7, 2 },
					{ 7, 7 },
					
				{ 2, 7 },
					{ 3, 3 },
					{ 6, 3 },
					
					{ 6, 6 },
					{ 3, 6 },
					{ 4, 4 },
					
					{ 5, 4 },
					{ 5, 5 },
					{ 4, 5 }
				};		
		
		int array[] = { 0, 4, 8, 12, 13, 9, 5, 1, 2, 6, 10, 14, 15, 11, 7, 3 };
		
	double custo = 0.0;
		
		for(int x=0;x<array.length;x++){
			if(x!=0 )custo += dist(cidades[array[x-1]], cidades[array[x]]); 
		}
		System.out.println(custo + " COST ");
		
	}
	
	@Override
	public List<Integer> call() throws RemoteException {
		solution.addAll(atPath);

		if(atPath.size() > LIMIT) {
			double cost = 0.0;
			for(int x=0;x<atPath.size()-1;x++){
				cost += dist(cities[atPath.get(x)], cities[atPath.get(x+1)] ); 
			}

			perm(atPath.get(atPath.size()-1),cost);

			if(solution.size() != cities.length){


				List<Integer> list = new ArrayList<Integer>();
				list.addAll(atPath);

				boolean listUsed[] = new boolean[cities.length];
				for(int x=0;x<listUsed.length;x++){
					listUsed[x] = false;
				}

				for(Integer i : list)listUsed[i] = true;

				double costForList = 0.0;
				for(int x=0;x<cities.length;x++)if(!listUsed[x]) list.add(x);	

				for(int x=0;x<list.size();x++){
					if(x!=0) costForList += dist(cities[list.get(x-1)], cities[list.get(x)]);	
				}

				setShared(new DoubleShared(new Double(costForList)));
				return list;
			}else{
				setShared(new DoubleShared(new Double(solutionCost)));
				return solution;
			}

		}else{
			return solution;
		}

	}

	public void perm(int at, double weight) throws RemoteException{

		Double sharedValue = (Double) getShared().get();	

		if(weight > sharedValue){
			return;
		}

		if(atPath.size() == used.length){

			weight += dist(cities[atPath.get(0)], cities[at]);

			if(solution.isEmpty()){
				solution.addAll(atPath);
			}else if(weight < solutionCost){
				solution.clear();
				solution.addAll(atPath);
				solutionCost = weight;
				setShared(new DoubleShared(new Double(solutionCost)));
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

	public static double dist(double p1[], double p2[]){
		double x = p1[0] - p2[0];
		double y = p1[1] - p2[1];
		return Math.sqrt(x*x + y*y);
	}

	public double[][] getCities(){
		return this.cities;
	}

	@Override
	public Map<String, Object> sucessorMap() {
		if(atPath.size()<=LIMIT) {
			return null;
		}

		Map<String, Object> rmap = new HashMap<String,Object>();

		rmap.put("path", solution);
		rmap.put("cost", solutionCost);
		rmap.put("tOne", getTOne());
		rmap.put("tInf", getTInf());

		String newId = "C";
		String[] ar = getId().split("_");

		for(int x=1;x<ar.length -1;x++){
			newId += ("_" + ar[x]);
		}

		rmap.put("parentId", newId);

		return rmap;		
	}

	@Override
	public List<Task<?>> generateChildren() {

		if(atPath.size() > LIMIT) return null;

		ArrayList<Task<?>> list = new ArrayList<Task<?>>();
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

		list.add(new ComposeTaskEuclideanTsp(count,id, getTOne(), getTInf()));//compose task is here, need to give it tOne and tInf

		return list;
	}	

}





