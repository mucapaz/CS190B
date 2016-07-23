package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Computer extends Remote{
	public static final int PORT = 62003;
	public static final String SERVICE_NAME = "Task";
	
	<T> T execute(Task<T> t) throws RemoteException;
	
}
