package client.decomposition;

import java.util.ArrayList;
import java.util.List;

import api.Task;
import tasks.TaskEuclideanTsp;

public class EuclideanTspDecomposer implements Decomposer{

	@Override
	public List<Task> decompose(Task task) {
		double[][] cities = ((TaskEuclideanTsp) task).getCities();
		
		List<Task> list = new ArrayList<Task>();
		
		for(int x=1;x<cities.length;x++){
			list.add(new TaskEuclideanTsp(cities, x));
		}
	
		return list;
	}
	
}
