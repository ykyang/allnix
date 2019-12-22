package org.allnix.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ./gradlew -PmainClass=org.allnix.rmi.MessengerServiceClient runApp 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class MessengerServiceClient {

	static public void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry();

		MessengerService server = (MessengerService) registry.lookup("MessengerService");
		String responseMessage = server.sendMessage("Client Message");
		String expectedMessage = "Server Message";

		System.out.println(responseMessage);
		
	}
}
