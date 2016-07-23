package system;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import api.Result;
import api.Space;
import api.Task;

public class ComputerImpl extends UnicastRemoteObject implements Computer{
	
	private Space space;
	
	public ComputerImpl(Space space) throws RemoteException{
		super();
		this.space = space;
		space.register(this);
	}
	
	@Override
	public <T> Result<T> call(Task<T> t) throws RemoteException {
		final long taskStartTime = System.nanoTime();
		T obj = t.call();
        final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;
        
        return new Result(obj, taskRunTime, t.getArgs());
	}

	
	public static void main(String[] args) {
		System.setSecurityManager( new SecurityManager() );
		String url = "rmi://" + args[0] + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
		
		Space space;
		try {
			space = (Space) Naming.lookup( url );
			ComputerImpl computerImpl = new ComputerImpl(space);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
