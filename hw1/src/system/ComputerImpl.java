package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import api.Computer;
import api.Task;

public class ComputerImpl implements Computer{
	
	public ComputerImpl(){
		super();
	}
	
	@Override
	public <T> T execute(Task<T> t) throws RemoteException {
		return t.execute();
	}

	public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Computer engine = new ComputerImpl();
            
            Computer stub =
                (Computer) UnicastRemoteObject.exportObject(engine, Computer.PORT);
            Registry registry = LocateRegistry.createRegistry(Computer.PORT);
            registry.rebind(Computer.SERVICE_NAME, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
	
}
