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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import tasks.TaskEuclideanTsp;
import tasks.TaskFibonacci;

/**
 *
 * @author Peter Cappello
 */
public class ClientFibonacci extends Client<Integer>
{
	private static Integer NUM_FIBONACCI = 20;
	
	
	public ClientFibonacci(String ip) throws RemoteException, NotBoundException, MalformedURLException
	{ 
		
		super( ip, new TaskFibonacci(NUM_FIBONACCI, 0, "" + NUM_FIBONACCI), "C_" + NUM_FIBONACCI );
	}
	
	

	public static void main( String[] args ) throws Exception
	{
		
		NUM_FIBONACCI = Integer.parseInt(args[1]);
		//System.out.println(NUM_FIBONACCI + " hey ");

		System.setSecurityManager( new SecurityManager() );
		final ClientFibonacci client = new ClientFibonacci(args[0]);
		client.init( "Fibonacci" );
		System.out.println(client.runTask());
	}
}

