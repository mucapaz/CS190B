package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Result;
import api.Task;

public interface Computer extends Remote{
	
	<T> Result<T> call(Task<T> t) throws RemoteException;
	
}
