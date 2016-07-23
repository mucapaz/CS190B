package tasks;

import java.io.Serializable;
import java.util.HashMap;

import api.Task;

public class TaskMandelbrotSet extends Task<Integer[][]> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final double LOWER_LEFT_X; // - C
    private final double LOWER_LEFT_Y; // - C
    private final double EDGE_LENGTH;
    private final int N_PIXELS;
    private final int ITERATION_LIMIT;
    private final double[] C;
    private final int di,dj, bx, by;
    
	
	public TaskMandelbrotSet(double LOWER_LEFT_X, double LOWER_LEFT_Y, double EDGE_LENGTH, int N_PIXELS,
            int ITERATION_LIMIT, int bx, int by ){
		this.bx = bx;
		this.by = by;
		this.di = (bx)*N_PIXELS/4;
		this.dj = (by)*N_PIXELS/4;
		
		args = new HashMap<String,String>();
		
		args.put("id", bx +" " + by);
				
		this.LOWER_LEFT_X = LOWER_LEFT_X;
		this.LOWER_LEFT_Y = LOWER_LEFT_Y;
		this.EDGE_LENGTH = EDGE_LENGTH;
		this.N_PIXELS = N_PIXELS;
		this.ITERATION_LIMIT = ITERATION_LIMIT;
		C = new double[]{LOWER_LEFT_X, LOWER_LEFT_Y};
	}


	
	@Override
	public Integer[][] call() {
		return function();
	}
	
	public Integer[][] function(){
		Integer[][] ar = new Integer[N_PIXELS/4][N_PIXELS/4];
		
		double var = EDGE_LENGTH/((double)(N_PIXELS));
			
		for(int x=0;x<N_PIXELS/4;x++){
			for(int y=0;y<N_PIXELS/4;y++){
				ar[x][y] = mandelbrot(x,y,var);
			}
		}
		
		return ar;
	}
	
	public int mandelbrot(int i, int j, double var){
		double zk[] = new double[2];
		double zk2[] = new double[2];
		
		zk[0] = LOWER_LEFT_X + var*(i + di); // real
		zk[1] = LOWER_LEFT_Y + var*(j + dj); // imaginary
		
		
		zk2[0] = LOWER_LEFT_X + var*(i + di); // real
		zk2[1] = LOWER_LEFT_Y + var*(j + dj); // imaginary
		
		for (int k=0; k< ITERATION_LIMIT; k++){
			if (absoluteValue(zk) > 2) return k;
			zk = sum(square(zk),zk2);		
		}
		return ITERATION_LIMIT;
	}

	public double[] square(double[] number){
		double[] ret = new double[2];
		ret[0] = number[0]*number[0] - number[1]*number[1];
		ret[1] = 2*number[0]*number[1];
		
		return ret;
	}
	
	public double[] sum(double[] number1, double[] number2){
		double[] ret = new double[2];
		ret[0] = number1[0] + number2[0];
		ret[1] = number1[1] + number2[1];
		
		return ret;
	}
	
	public double absoluteValue(double[] number){
		double value = number[0]*number[0] + number[1]*number[1];
		value = Math.sqrt(value);
		return value;
	}



	public double getLOWER_LEFT_X() {
		return LOWER_LEFT_X;
	}



	public double getLOWER_LEFT_Y() {
		return LOWER_LEFT_Y;
	}



	public double getEDGE_LENGTH() {
		return EDGE_LENGTH;
	}



	public int getN_PIXELS() {
		return N_PIXELS;
	}



	public int getITERATION_LIMIT() {
		return ITERATION_LIMIT;
	}



	public double[] getC() {
		return C;
	}



	public int getDi() {
		return di;
	}



	public int getDj() {
		return dj;
	}



	public int getBx() {
		return bx;
	}



	public int getBy() {
		return by;
	}
	
	
}
