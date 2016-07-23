package system;

import api.Shared;

public class DoubleShared extends Shared{
	
	public DoubleShared(Double value){
		super(value);
	}
	
	@Override
	public boolean isNewer(Shared that) { // returns true if this shared object is newer than that shared object.
		Double sharedValue = (Double) this.get();
		Double thatValue = (Double) that.get();
		
		return  sharedValue < thatValue;				
	}

}
