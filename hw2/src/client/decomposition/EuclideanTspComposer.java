package client.decomposition;

import java.util.ArrayList;
import java.util.List;

import api.Result;

public class EuclideanTspComposer implements Composer<List<Integer>>{

	@Override
	public List<Integer> compose(List<Result<List<Integer>>> results) {
		
		List<Integer> list = new ArrayList<Integer>();
	
		double bestWeight = Double.POSITIVE_INFINITY;
		
		
		
		for(Result<List<Integer>> result : results){
			
			
			if(bestWeight > Double.parseDouble(result.mapValue("cost"))){
				bestWeight = Double.parseDouble(result.mapValue("cost"));
				list = result.getTaskReturnValue();
			}
			
		}
		
		return list;
	}

}
