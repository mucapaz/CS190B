/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import api.Result;
import api.Shared;
import api.Space;
import api.Task;
import system.ComputerImpl;
import system.DoubleShared;

/**
 *
 * @author Peter Cappello
 * @param <T> return type the Task that this Client executes.
 */
public class Client<T> extends JFrame
{
    final protected Task task;
    final private Space space;
    final String FINAL_ID;
    
    public Client( final String domainName, final Task task, final String FINAL_ID) 
            throws RemoteException, NotBoundException, MalformedURLException
    {     
    	this.FINAL_ID = FINAL_ID;
        this.task = task;       
        String url = "rmi://" + domainName + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        space = (Space) Naming.lookup( url );
        
        space.setShared(new DoubleShared(Double.MAX_VALUE));
        
        
    }
    
    public Client( final String domainName, final Task task, final String FINAL_ID, Shared shared) 
            throws RemoteException, NotBoundException, MalformedURLException
    {     
    	this.FINAL_ID = FINAL_ID;
        this.task = task;       
        String url = "rmi://" + domainName + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        space = (Space) Naming.lookup(url);
        
        //space.setShared(new DoubleShared(Double.MAX_VALUE));
        
        
    }
    
    void init( final String title )
    {
        this.setTitle( title );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    
    public void add( final JLabel jLabel )
    {
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel ), BorderLayout.CENTER );
        pack();
        setVisible( true );
    }
    
    public T runTask(Shared shared) throws RemoteException
    {
    	final long taskStartTime = System.nanoTime();
    	
    	space.setShared(shared);
        space.putTask( task, FINAL_ID);
        
        
        Result result = space.take();
        

        final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;
        
       Logger.getLogger( Client.class.getCanonicalName() )
        .log( Level.INFO, "Client (Starting with task {0}) total execution time: {1} T1 : {2} T infinite : {3}", new Object[]{task, taskRunTime, result.getTOne(), result.getTInf() } );
              
        
        return (T) result.getTaskReturnValue();
    }
}
