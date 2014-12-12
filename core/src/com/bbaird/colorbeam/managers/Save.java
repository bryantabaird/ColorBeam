package com.bbaird.colorbeam.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Save {
	public static AppData ad;
	
	public static void save() {
		
		String server = "www.haydenlakept.com";
        int port = 21;
        String user = "haydenla";
        String pass = "Bryguy123!@#";
		
		FTPClient ftpClient = new FTPClient();
		
		try {
			FileHandle file = Gdx.files.local("currentprogress.sav");
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(b);
			out.writeObject(ad);
			byte[] array = b.toByteArray();
			out.close();
			file.writeBytes(array, false);
			
			// new
			
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
            // APPROACH #1: uploads first file using an InputStream
            FileHandle f = (Gdx.files.local("currentprogress.sav"));
            File firstLocalFile = new File(f.name());
            
            String firstRemoteFile = "/public_html/cs/currentprogress.sav";
            InputStream inputStream = new FileInputStream(firstLocalFile);
 
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
			
			// new
		}
		catch(Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}	
	}
	
	public static void load() {
		
		String server = "www.haydenlakept.com";
        int port = 21;
        String user = "haydenla";
        String pass = "Bryguy123!@#";
		
		FTPClient ftpClient = new FTPClient();
		
		try {
			if(!saveFileExists()) {
				System.out.println("doesn't exist");
				init();
				return;
			}
			//FileHandle file = Gdx.files.local("currentprogress.sav");

			
			// new
			
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            
            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "/public_html/cs/currentprogress.sav";
            FileHandle file1 = Gdx.files.local("currentprogress.sav");
            File downloadFile1 = new File(file1.name());
            OutputStream outputStream1 = new FileOutputStream(downloadFile1);
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
 
            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }
			
			//
			ByteArrayInputStream b = new ByteArrayInputStream(file1.readBytes());
			ObjectInput in = new ObjectInputStream(b);
			
			ad = (AppData) in.readObject();
			in.close();	
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean saveFileExists() {
		FileHandle f = (Gdx.files.local("currentprogress.sav"));
		//return f.exists();
		return true;
	}
	
	public static void init() {
		ad = new AppData();
		save();
	}
}
