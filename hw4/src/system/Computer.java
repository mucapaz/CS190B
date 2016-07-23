package system;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import api.Result;
import api.Task;

public interface Computer extends Remote{
	
	List<Result<?>> call(List<Task<?>> t) throws RemoteException;
 
	void registerComputerProxy(ComputerProxy computerProxy) throws RemoteException;

	void putTask(Task task) throws RemoteException;
	
}
