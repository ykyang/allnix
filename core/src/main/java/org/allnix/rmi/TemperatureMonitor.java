package org.allnix.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * ./gradlew -PmainClass=org.allnix.rmi.TemperatureMonitor runApp
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class TemperatureMonitor extends UnicastRemoteObject implements TemperatureListener {
	protected TemperatureMonitor() throws RemoteException {
	}

	private static final long serialVersionUID = 1L;

	static public void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry();
		TemperatureServer server = (TemperatureServer) registry.lookup("TemperatureService");
		
		System.out.println(String.format("First Temperature: %g", server.getTemperature()));
		
		TemperatureMonitor monitor = new TemperatureMonitor();
		server.addTemperatureListener(monitor);
	}

	@Override
	public void temperatureChanged(double temperature) throws RemoteException {
		System.out.println("Temperature changed: " + temperature);
	}

	
}
