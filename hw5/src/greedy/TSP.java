package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class Edge{
	
	public int v1,v2;
	public double dist;
	
	public Edge(int v1, int v2, double dist){
		this.v1 = v1;
		this.v2 = v2;
		this.dist= dist;
	}
			
}

class EdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge e1, Edge e2) {
    	
    	if(e1.dist < e2.dist ){
    		return -1;
    	}else if(e1.dist > e2.dist){
    		return +1;
    	}else{
    		if(e1.v1 < e2.v1){
    			return -1;
    		}else if(e1.v1 > e2.v1){
    			return +1;
    		}else{
    			
    			if(e1.v2 < e2.v2){
    				return -1;
    			}else if(e1.v2 > e2.v2){
    				return +1;
    			}else{
    				return 0;
    			}
    			
    		}
    	}
    	
    	
    }
}

public class TSP {
	
	
	List<Edge> ar;
	private double[][] cities;
	int[] degree;
	
	public TSP(double cities[][] ){
		this.cities = cities;
		
		degree = new int[cities.length];
		ar = new ArrayList<Edge>();
				
		for(int x=0;x<cities.length;x++){
			for(int y = x +1; y <cities.length; y++){
				ar.add(new Edge(x,y, dist(cities[x],cities[y])));
			}
		}
		
		
		
	}

	public double execute(){
		
		UnionFind uf = new UnionFind(cities.length);
		Collections.sort(ar, new EdgeComparator());
		double totalCost = 0.0;
		int count = 0;
		for(int x=0;x<ar.size();x++){
			if(!uf.isSameSet(ar.get(x).v1, ar.get(x).v2) && degree[ar.get(x).v1] < 2 && degree[ar.get(x).v2] < 2){
				totalCost += ar.get(x).dist;
				uf.unionSet(ar.get(x).v1, ar.get(x).v2);
				degree[ar.get(x).v1]++;
				degree[ar.get(x).v2]++;
				count++;
			}
			
		}
		int v1 = -1, v2 = -1;
		for(int x=0;x<cities.length;x++){
			if(degree[x] < 2){
				if(v1 == -1) v1 = x;
				else v2 = x;
			}
		}
		
		totalCost += dist(cities[v1], cities[v2]);
		
		return totalCost;
	}
	
	public static double dist(double p1[], double p2[]){
		double x = p1[0] - p2[0];
		double y = p1[1] - p2[1];
		return Math.sqrt(x*x + y*y);
	}
	
	
}
