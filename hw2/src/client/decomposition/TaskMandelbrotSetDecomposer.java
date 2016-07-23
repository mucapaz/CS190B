package client.decomposition;

import java.util.ArrayList;
import java.util.List;

import api.Task;
import tasks.TaskMandelbrotSet;

public class TaskMandelbrotSetDecomposer implements Decomposer{

	@Override
	public List<Task> decompose(Task task) {
		List<Task> list =  new ArrayList<Task>();
		
		TaskMandelbrotSet tm = (TaskMandelbrotSet) task;
		
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				list.add(new TaskMandelbrotSet(tm.getLOWER_LEFT_X(),
						tm.getLOWER_LEFT_Y(),
						tm.getEDGE_LENGTH(),
						tm.getN_PIXELS(),
						tm.getITERATION_LIMIT(),
						x,
						y
					));
			}
		}
		return list;		
	}

}
