package client.decomposition;

import java.util.List;

import api.Result;

public class TaskMandelbrotSetComposer implements Composer<Integer[][]>{

	@Override
	public Integer[][] compose(List<Result<Integer[][]>> results) {
		Integer[][] ma = new Integer[results.get(0).getTaskReturnValue().length*4]
				[results.get(0).getTaskReturnValue().length*4];

		Integer[][] resultMa;


		int length = results.get(0).getTaskReturnValue().length;

		for(Result<Integer[][]> result : results){
			resultMa = result.getTaskReturnValue();
			String id = result.mapValue("id");
			String lstr[] = id.split(" ");
			int bx = Integer.parseInt(lstr[0]), by = Integer.parseInt(lstr[1]);

			for(int x=0;x<length;x++){
				for(int y=0;y<length;y++){
					ma[x + bx*length][y + by*length] = resultMa[x][y];
				}
			}

		}
		
		return ma;
	}
}





