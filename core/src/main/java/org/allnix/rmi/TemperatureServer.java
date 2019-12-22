package org.allnix.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TemperatureServer extends Remote {
	void addTemperatureListener(TemperatureListener listener) throws RemoteException;

	void removeTemperatureListener(TemperatureListener listener) throws RemoteException;

	Double getTemperature() throws RemoteException;
}
