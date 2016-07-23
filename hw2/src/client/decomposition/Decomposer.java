package client.decomposition;

import java.util.List;

import api.Task;

public interface Decomposer<T> {

	public List<Task<T>> decompose(Task<T> task);
	
}
