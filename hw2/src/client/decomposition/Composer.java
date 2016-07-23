package client.decomposition;

import java.util.List;

import api.Result;

public interface Composer<T> {

	public T compose(List<Result<T>> results);
	
}
