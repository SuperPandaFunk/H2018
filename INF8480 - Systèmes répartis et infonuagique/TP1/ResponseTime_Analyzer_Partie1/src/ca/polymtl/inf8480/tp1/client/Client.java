package ca.polymtl.inf8480.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;

public class Client {
	public static void main(String[] args) {
		String distantHostname = null;
		int sizeOctet = 0;
		if (args.length > 0) {
			distantHostname = args[0];
			if (args.length > 1) {
				sizeOctet = Integer.parseInt(args[1]);
				if (sizeOctet <= 0 || sizeOctet > 7) {
					System.out.println("Le nombre doit etre comprit entre 1 et 7");
					return;
				}
			}
		}

		Client client = new Client(distantHostname);
		client.run(convertSize(sizeOctet));
	}

	FakeServer localServer = null; // Pour tester la latence d'un appel de
									// fonction normal.
	private ServerInterface localServerStub = null;
	private ServerInterface distantServerStub = null;

	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		localServer = new FakeServer();
		localServerStub = loadServerStub("127.0.0.1");

		if (distantServerHostname != null) {
			distantServerStub = loadServerStub(distantServerHostname);
		}
	}

	private void run(int size) {
		appelNormal(size);

		if (localServerStub != null) {
			appelRMILocal(size);
		}

		if (distantServerStub != null) {
			appelRMIDistant(size);
		}
	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("server");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage()
					+ "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}

		return stub;
	}

	private void appelNormal(int size) {
		long start = System.nanoTime();
		int result = localServer.test( new byte[size]);
		long end = System.nanoTime();

		System.out.println("Temps écoulé appel normal: " + (end - start)
				+ " ns");
		System.out.println("Résultat appel normal: " + result);
	}

	private void appelRMILocal(int size) {
		try {
			long start = System.nanoTime();
			int result = localServerStub.test( new byte[size]);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI local: " + (end - start)
					+ " ns");
			System.out.println("Résultat appel RMI local: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void appelRMIDistant(int size) {
		try {
			long start = System.nanoTime();
			int result = distantServerStub.test( new byte[size]);
			long end = System.nanoTime();

			System.out.println("Temps écoulé appel RMI distant: "
					+ (end - start) + " ns");
			System.out.println("Résultat appel RMI distant: " + result);
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}
	
	private static int convertSize(int sizeOctet){
		return (int)Math.pow(10,sizeOctet);
	}
}
