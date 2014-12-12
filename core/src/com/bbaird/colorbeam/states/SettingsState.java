package com.bbaird.colorbeam.states;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.bbaird.colorbeam.managers.GameStateManager;

public class SettingsState extends GameState {

	//private Stage stage;
	//private TextureRegion logo;
	//private SpriteBatch batch;
	private AssetManager manager;
	//private Texture tex1;
	
	public SettingsState(GameStateManager gsm) {
		super(gsm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		String server = "www.haydenlakept.com";
        int port = 21;
        String user = "haydenla";
        String pass = "Bryguy123!@#";
 
        //upload
        FTPClient ftpClient = new FTPClient();
        try {
 
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
 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        //download
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
 
           // File temp = new File("http://www.haydenlakept.com/cs/currentprogress.sav");
            
            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "/Projects.zip";
            File downloadFile1 = new File("C:/Users/Bryant/Desktop/temp.zip");
            OutputStream outputStream1 = new FileOutputStream(downloadFile1);
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
 
            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }
 
            
 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
//		 Resolution[] resolutions = new Resolution[4];
//         resolutions[0] = new Resolution(500, 300, "500x300");
//         resolutions[1] = new Resolution(800, 480, "800x480");
//         resolutions[2] = new Resolution(1280, 768, "1280x768");
//         resolutions[3] = new Resolution(1920, 1152, "1920x1152");
//         ResolutionFileResolver resolver = new ResolutionFileResolver(
//                 new InternalFileHandleResolver(), resolutions);
//         manager = new AssetManager();
//         manager.setLoader(Texture.class, new TextureLoader(resolver));
//         load();
//         Texture.setAssetManager(manager);
//		
//		
//		batch = new SpriteBatch();
		
	}
	
	public void load() {
		//tex1 = new Texture("images/star.png");
		manager.load("images/star.png", Texture.class);
	}
//		
//		
//		
//		
//		
//		
//		
//		
//		stage = new Stage();
//		Gdx.input.setInputProcessor(stage);
//		Texture t = new Texture("images/triangle.png");
//		final Skin skin = new Skin();
//		skin.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
//		skin.add("badlogic", t);
//
//		Image sourceImage = new Image(skin, "badlogic");
//		sourceImage.setBounds(50, 125, t.getWidth(), t.getHeight());
//		stage.addActor(sourceImage);
//
//		Image validTargetImage = new Image(skin, "badlogic");
//		validTargetImage.setBounds(200, 50, t.getWidth(), t.getHeight());
//		stage.addActor(validTargetImage);
//
//		Image invalidTargetImage = new Image(skin, "badlogic");
//		invalidTargetImage.setBounds(200, 200, t.getWidth(), t.getHeight());
//		stage.addActor(invalidTargetImage);
//
//		DragAndDrop dragAndDrop = new DragAndDrop();
//		dragAndDrop.addSource(new Source(sourceImage) {
//			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
//				Payload payload = new Payload();
//				payload.setObject("Some payload!");
//
//				payload.setDragActor(new Label("Some payload!", skin));
//
//				Label validLabel = new Label("Some valid payload!", skin);
//				validLabel.setColor(0, 1, 0, 1);
//				payload.setValidDragActor(validLabel);
//
//				Label invalidLabel = new Label("Some invalid payload!", skin);
//				invalidLabel.setColor(1, 0, 0, 1);
//				payload.setInvalidDragActor(invalidLabel);
//
//				return payload;
//			}
//		});
//		
//		
//		Target temp = new Target(validTargetImage) {
//			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
//				getActor().setColor(Color.GREEN);
//				return true;
//			}
//
//			public void reset (Source source, Payload payload) {
//				getActor().setColor(Color.BLUE);
//			}
//
//			public void drop (Source source, Payload payload, float x, float y, int pointer) {
//				System.out.println("Accepted: " + payload.getObject() + " " + x + ", " + y);
//			}
//		};
//		
//		dragAndDrop.addTarget(temp);
//		
//		dragAndDrop.addTarget(new Target(invalidTargetImage) {
//			public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
//				getActor().setColor(Color.RED);
//				return false;
//			}
//
//			public void reset (Source source, Payload payload) {
//				getActor().setColor(Color.WHITE);
//			}
//
//			public void drop (Source source, Payload payload, float x, float y, int pointer) {
//			}
//		});
//		
//		
//		InputMultiplexer inputMultiplexer = new InputMultiplexer();
//		inputMultiplexer.addProcessor(stage);
//		inputMultiplexer.addProcessor(backButtonAdapter);
//        Gdx.input.setInputProcessor(inputMultiplexer);
//	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		//stage.act();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		//stage.draw();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//stage.dispose();
	}

}
