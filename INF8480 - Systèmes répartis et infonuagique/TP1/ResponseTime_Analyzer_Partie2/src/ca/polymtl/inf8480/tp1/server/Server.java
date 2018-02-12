package ca.polymtl.inf8480.tp1.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.io.*;
import java.nio.*;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.LockMessage;
import ca.polymtl.inf8480.tp1.shared.StubFile;

public class Server implements ServerInterface {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

	public Server() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Server ready.");
		} catch (ConnectException e) {
			System.err
					.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	private final String clientCredential = "clientID.txt";
	private final String lockFile = "lock.txt";

	private void createCredentialFile(){
		File fileToCheck = new File(clientCredential);
		if (!fileToCheck.exists()){
			try{
					FileOutputStream fos = new FileOutputStream(clientCredential);
					fos.write((byte)1);
					fos.close();
			}catch (Exception ex){
				System.out.println("Erreur: " + ex.getMessage());
			}
		}
	}

	/*
	 * Méthode accessible par RMI. 
	 */
	@Override
	public StubFile CreateClientID() throws RemoteException {
		File fileToCheck = new File(clientCredential);
		int i = 0;
		StubFile toReturn = null;
		createCredentialFile();

		try{
			byte[] array = java.nio.file.Files.readAllBytes(fileToCheck.toPath());
			i = array[0];
			toReturn = new StubFile(clientCredential);
			byte[] tmp = new byte[1];
			tmp[0] = (byte)i++;
			toReturn.setData(tmp);
		}catch (Exception io){
			System.out.println("Erreur: " + io.getMessage());
		}
		
		try{
			fileToCheck.delete();
			FileOutputStream fos = new FileOutputStream(clientCredential);
			fos.write((byte)i);
			fos.close();
		}catch (Exception ex){
			System.out.println("Erreur: " + ex.getMessage());
		}

		return toReturn;
	}
	
	public boolean create(String nom) throws RemoteException {
		File file = new File("files/" + nom);
		if (file.exists()){
			return false;
		}
		try{
			FileOutputStream out = new FileOutputStream("files/" + nom);
			out.close();

			try{
				File fileToCheck = new File(lockFile);
				java.nio.file.Files.write(fileToCheck.toPath(), (nom + '\n' + '0' + '\n').getBytes(), java.nio.file.StandardOpenOption.APPEND);
			}catch (Exception io){
				System.out.println("Erreur: " + io.getMessage());
			}

			return true;
		} catch (Exception FileNotFoundException){}
		//should never execute
		return false;
	}

	public ArrayList<StubFile> list(){
		/*ArrayList<StubFile> toReturn = new ArrayList<>();
		final File folder = new File("files");
		for (final File fileEntry : folder.listFiles()){
			StubFile tmpFile = new StubFile(fileEntry.getName());
			//TODO Ajouter les locks
			toReturn.add(tmpFile);
		}*/

		ArrayList<StubFile> toReturn = new ArrayList<>();
		File file = new File(lockFile);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				StubFile tmpFile = new StubFile(line);
				tmpFile.setLockID(Integer.parseInt(br.readLine()));
				toReturn.add(tmpFile);
			}
		}catch (Exception e){
			System.out.println("Erreur: " + e.getMessage());
			return null;
		}
		return toReturn;
	}

	public StubFile get(String nom, byte[] checkSum){
		File file = new File("files/" + nom);
		if (!file.exists()){
			return null;
		}
		
		try{
			byte[] array = java.nio.file.Files.readAllBytes(file.toPath());
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			if (!Arrays.toString(md.digest(array)).equals(Arrays.toString(checkSum))){
				StubFile toReturn = new StubFile(nom);
				toReturn.setData(array);
				return toReturn;
			}
		}
		catch(Exception e){
			return null;
		}
		return null;
	}

	public ArrayList<StubFile>  sync()
	{
		ArrayList<StubFile> toReturn = new ArrayList<>();
		File folder = new File("files");
		File[] listOfFiles = folder.listFiles();

		for(int i =0; i < listOfFiles.length; ++i)
		{
			if (listOfFiles[i].isFile())
			{
				toReturn.add(get(listOfFiles[i].getName(),null));
			}
		}

		return toReturn;
	}

	public LockMessage lock(String nom, int clientID, byte[] checkSum){
		File file = new File("files/" + nom);
		if (!file.exists()){
			return new LockMessage(false, "Le fichier n'existe pas.");
		}
		
		try{
			byte[] array = java.nio.file.Files.readAllBytes(file.toPath());
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			if (!Arrays.toString(md.digest(array)).equals(Arrays.toString(checkSum))){
				return new LockMessage(false, "Le fichier n'est pas a jour.");
			}
			if (isAlreadyLock(nom) != 0){
				return new LockMessage(false, "Le fichier est deja blocker par le client " + isAlreadyLock(nom) + ".");
			}
			changeLock(nom, clientID, false);
			return new LockMessage(true, "");
		}
		catch(Exception e){
			return null;
		}
	}

	private int isAlreadyLock(String nom){
		File file = new File(lockFile);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			boolean found = false;
			while ((line = br.readLine()) != null || !found) {
				if (line.equals(nom)){
					String line2 = br.readLine();
					found = true;
					return Integer.parseInt(line2);
				}
			}
			return 0;
		}catch (Exception e){
			System.out.println("Erreur: " + e.getMessage());
			return 0;
		}
	}

	private void changeLock(String nom, int id, boolean unlock){
		try {
			BufferedReader file = new BufferedReader(new FileReader(lockFile));
			String line;
			StringBuffer inputBuffer = new StringBuffer();

			while ((line = file.readLine()) != null){
				inputBuffer.append(line);
				inputBuffer.append('\n');
			}

			String inputStr = inputBuffer.toString();

			file.close();

			if (unlock){
				inputStr = inputStr.replace(nom + '\n' + String.valueOf(id), nom + '\n' + '0');
			}else {
				inputStr = inputStr.replace(nom + '\n' + '0', nom + '\n' + String.valueOf(id));
			}

			FileOutputStream fos = new FileOutputStream(lockFile);
			fos.write(inputStr.getBytes());
			fos.close();
		}catch (Exception e){
			System.out.println("Erreur: " + e.getMessage());
		}
	}

	public boolean push(String nom, byte[] contenu, int clientID) {
		File file = new File("files/" + nom);
		if (!file.exists() || isAlreadyLock(nom) != clientID){
			return false;
		}
		file.delete();
		try{
			FileOutputStream fos = new FileOutputStream("files/" + nom);
			fos.write(contenu);
			fos.close();
		}catch (Exception ex){
			System.out.println("Erreur: " + ex.getMessage());
		}
		changeLock(nom, clientID, true);
		return true;
	}
}
