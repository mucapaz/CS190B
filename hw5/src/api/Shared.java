package api;

import java.io.Serializable;
import java.rmi.Remote;

public abstract class Shared implements Serializable{

	private Object obj;
	
	public Shared(Object obj){
		this.obj = obj;
	}
	
	public abstract boolean isNewer(Shared shared);
	
	public Object get(){
		return this.obj;
	}
	
	public void setShared(Shared shared){
		this.obj = shared.get();
	}
	
}
