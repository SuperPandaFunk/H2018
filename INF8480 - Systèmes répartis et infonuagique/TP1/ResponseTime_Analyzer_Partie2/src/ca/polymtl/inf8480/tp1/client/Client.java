package ca.polymtl.inf8480.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.io.*;
import java.nio.*;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.StubFile;
import ca.polymtl.inf8480.tp1.shared.LockMessage;

public class Client {
	public static void main(String[] args) {
		String distantHostname = "132.207.12.109";
		Client client = new Client(distantHostname);
		client.createClientID();
		if (args.length > 0) {			
			switch (args[0]){
				case "list":
					client.list();
					break;
				case "syncLocalDirectory":
					client.sync();
					break;
				case "create":
					if (args.length > 1)
						client.create(args[1]);
					else
						System.out.println("Veuillez entrer un nom de fichier");
					break;
				case "get":
				if (args.length > 1)
						client.get(args[1]);
					else
						System.out.println("Veuillez entrer un nom de fichier");
					break;
				case "lock":
					if (args.length > 1)
						client.lock(args[1]);
					else
						System.out.println("Veuillez entrer un nom de fichier");
					break;
				case "push":
					if (args.length > 1)
						client.push(args[1]);
					else
						System.out.println("Veuillez entrer un nom de fichier");
					break;
				default:
					System.out.println("Sum ting wong... rip");
				return;
			}
		}
	}

	private ServerInterface distantServerStub = null;
	private final String clientCredential = "clientID.txt";
	private int clientID = 0;

	public Client(String distantServerHostname) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		distantServerStub = loadServerStub(distantServerHostname);
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
	
	private void sync(){
		try{
			ArrayList<StubFile> allFiles = distantServerStub.sync();

			for(StubFile newFile : allFiles)
			{
				File fileToCheck = new File("files/" + newFile.getName());
				if (newFile != null){
					if (fileToCheck.exists())
						fileToCheck.delete();
					try{
						FileOutputStream fos = new FileOutputStream("files/" + newFile.getName());
						fos.write(newFile.getData());
						fos.close();
					}catch (Exception ex){
						System.out.println("Erreur: " + ex.getMessage());
					}
				}
			}
		}catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void create(String nom){
		try{
			if (distantServerStub.create(nom)){
				System.out.println("Le fichier: " + nom + " a ete creer.");
				return;
			}
			System.out.println("Le fichier existe deja");
		}catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void list(){
		try{
			ArrayList<StubFile> allFiles = distantServerStub.list();
			for(StubFile files : allFiles){
				System.out.println("* " + files.getName() + "\t" + files.getLockString());
			}
			System.out.println(allFiles.size() + " fichier(s)");
		}catch (RemoteException e){
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	private void get(String nom){
		StubFile newFile = null;
		try{
			File fileToCheck = new File("files/" + nom);

			if (fileToCheck.exists()){
				try{
					byte[] array = java.nio.file.Files.readAllBytes(fileToCheck.toPath());
					MessageDigest md = MessageDigest.getInstance("MD5");
					newFile = distantServerStub.get(nom, md.digest(array));
				}catch(Exception e2){}
			}else {
				newFile = distantServerStub.get(nom, null);
			}
			
			if (newFile != null){
				fileToCheck.delete();
				try{
					FileOutputStream fos = new FileOutputStream("files/" + nom);
					fos.write(newFile.getData());
					fos.close();
					System.out.println(nom + " synchroniser");
				}catch (Exception ex){
					System.out.println("Erreur: " + ex.getMessage());
				}
			}
			else{
				System.out.println(nom + " est a jour");
			}
		}catch (RemoteException e){
			System.out.println("Erreur: " + e.getMessage());
			return;
		}
	}

	private void createClientID(){
		File fileToCheck = new File(clientCredential);
		if (fileToCheck.exists()){
			try{
				byte[] encoded = java.nio.file.Files.readAllBytes(fileToCheck.toPath());
				clientID = encoded[0];
			}catch (Exception io ){
				System.out.println("Erreur: " + io.getMessage());
			}
		}else{
			try{
				StubFile tmpFile = distantServerStub.CreateClientID();
				FileOutputStream fos = new FileOutputStream(clientCredential);
				fos.write(tmpFile.getData());
				fos.close();
				clientID = tmpFile.getData()[0];
			}catch (Exception ex){
					System.out.println("Erreur: " + ex.getMessage());
			}
		}
	}

	private void lock(String nom){
		LockMessage response = null;
		try{
			File fileToCheck = new File("files/" + nom);
			if (!fileToCheck.exists()){
				System.out.println("Le fichier " + nom + " n'existe pas dans votre repertoire");
				return;
			}
			byte[] array = java.nio.file.Files.readAllBytes(fileToCheck.toPath());
			MessageDigest md = MessageDigest.getInstance("MD5");
			response = distantServerStub.lock(nom, clientID, md.digest(array));
		}catch(Exception ex){
			System.out.println("Erreur: " + ex.getMessage());
		}

		if (response.isSuccessful()){
			System.out.println("Le fichier " + nom + " est verrouille");
		}
		else{
			System.out.println(response.getMessage());
		}
	}

	private void push(String nom) {
		try{
			File fileToCheck = new File("files/" + nom);
			if (!fileToCheck.exists()){
				System.out.println("Le fichier " + nom + " n'existe pas dans votre repertoire");
				return;
			}
			if (distantServerStub.push(nom, java.nio.file.Files.readAllBytes(fileToCheck.toPath()), clientID)){
				System.out.println("Le fichier " + nom + " a ete envoyer sur le serveur");
			}else{
				System.out.println("Une erreur est suvenue. Avez-vous lock le fichier?");
			}
		}catch(Exception e){
			System.out.println("Erreur: " + e.getMessage());
		}
	}
}
