package org.allnix.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * ./gradlew -PmainClass=org.allnix.rmi.MessengerServiceImpl runApp 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class MessengerServiceImpl implements MessengerService {

	@Override
	public String sendMessage(String clientMessage) {
		return "Client Message".equals(clientMessage) ? "Server Message" : null;
	}
	
	public String unexposedMethod() {
		return null;
	}

	static public void main(String[] args) throws RemoteException, AlreadyBoundException {
		MessengerService server = new MessengerServiceImpl();
		// KEEP: for doc purpose
		// MessengerService stub = (MessengerService) UnicastRemoteObject.exportObject((MessengerService) server, 0);
		Remote stub = UnicastRemoteObject.exportObject((MessengerService) server, 0);
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.bind("MessengerService", stub);
		
		
		
        

	}
}
