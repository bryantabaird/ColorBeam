package com.bbaird.colorbeam.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.managers.AppData;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.Save;

public class MenuState extends GameState {
	
	private Sprite sprite;
	private Texture texture;
	private BitmapFont buttonFont;
	private BitmapFont titleFont;
	private SpriteBatch spriteBatch;
	private Stage stage;
	private TextButtonStyle buttonStyle;
	private LabelStyle labelStyle;
	
	private Label label;
	private TextButton newGameButton;
	private TextButton continueButton;
	private TextButton settingsButton;
	private TextButton levelButton;
	
	private final String TITLE = "ColorBeam";
	private final String BUTTON1 = "New Game";
	private final String BUTTON2 = "Continue";
	private final String BUTTON3 = "Settings";
	private final String BUTTON4 = "Levels";
	
	private final String FONTNAME = "Kristen32";
	private final String TITLEFONTNAME = "Kristen50";
	
	private float height;
	private float width;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void init() {
		
		stage = new Stage();
       
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(new InputAdapter() {
            public boolean keyDown(int keycode) {
                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK))
                	Gdx.app.exit();
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
		
        
		
		height = ColorBeam.HEIGHT;
		width = ColorBeam.WIDTH;
		float fontWidth;
		float fontHeight;
		
		texture = new Texture("images/texture1.jpg");
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		buttonFont = new BitmapFont(Gdx.files.internal("fonts/" + FONTNAME + ".fnt"), 
				Gdx.files.internal("fonts/" + FONTNAME + ".png"), false);
		titleFont = new BitmapFont(Gdx.files.internal("fonts/" + TITLEFONTNAME + ".fnt"), 
				Gdx.files.internal("fonts/" + TITLEFONTNAME + ".png"), false);
		buttonStyle = new TextButtonStyle();
		labelStyle = new LabelStyle();
		sprite = new Sprite(texture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch = new SpriteBatch();
		

		FileHandle filehandle = Gdx.files.internal("fonts/AnimeAceItalic.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(filehandle);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (24 * Gdx.graphics.getDensity());
		//buttonFont = generator.generateFont(parameter);
		parameter.size = (int) (32 * Gdx.graphics.getDensity());
		//titleFont = generator.generateFont(parameter);
		generator.dispose();
		
		buttonStyle.font = buttonFont;
		labelStyle.font = titleFont;
        
		fontWidth = titleFont.getBounds(TITLE).width;
		fontHeight = titleFont.getBounds(TITLE).height;
		label = new Label(TITLE, labelStyle);
		label.setPosition(width/2 - fontWidth/2, 3*height/4);
		
		fontWidth = buttonFont.getBounds(BUTTON1).width;
		fontHeight = buttonFont.getBounds(BUTTON1).height;
		newGameButton = new TextButton(BUTTON1, buttonStyle);
		newGameButton.setPosition(width/2 - fontWidth/2, height/2);
		newGameButton.setWidth(fontWidth);
		newGameButton.setHeight(fontHeight);
        newGameButton.addListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	if (Save.saveFileExists()) {
            		Gdx.files.local("currentprogress.sav").delete();
            		Save.ad = new AppData();
            	}
            	gsm.setState(GameStateManager.PLAY);
            }
        });
        
		fontWidth = buttonFont.getBounds(BUTTON2).width;
		fontHeight = buttonFont.getBounds(BUTTON2).height;
		continueButton = new TextButton(BUTTON2, buttonStyle);
		continueButton.setPosition(width/2 - fontWidth/2, 3*height/8);
		continueButton.setWidth(buttonFont.getBounds(BUTTON2).width);
		continueButton.setHeight(buttonFont.getBounds(BUTTON2).height);
        continueButton.addListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	gsm.setState(GameStateManager.PLAY);
            }
        });
        
		fontWidth = buttonFont.getBounds(BUTTON3).width;
		fontHeight = buttonFont.getBounds(BUTTON3).height;
//		settingsButton = new TextButton(BUTTON3, buttonStyle);
//		settingsButton.setPosition(width/2 - fontWidth/2, height/4);
//		settingsButton.setWidth(buttonFont.getBounds(BUTTON3).width);
//		settingsButton.setHeight(buttonFont.getBounds(BUTTON3).height);
//        settingsButton.addListener(new InputListener() {
//        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//            	return true;
//            }
//        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//        		Save.ad.setAdditive(!Save.ad.getAdditive());
//            	gsm.setState(GameStateManager.SETTINGS);
//            }
//        });
        
		fontWidth = buttonFont.getBounds(BUTTON4).width;
		fontHeight = buttonFont.getBounds(BUTTON4).height;
		levelButton = new TextButton(BUTTON4, buttonStyle);
		//levelButton.setPosition(width/2 - fontWidth/2, height/8);
		levelButton.setPosition(width/2 - fontWidth/2, height/4);
		levelButton.setWidth(buttonFont.getBounds(BUTTON4).width);
		levelButton.setHeight(buttonFont.getBounds(BUTTON4).height);
        levelButton.addListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	gsm.setState(GameStateManager.LEVELS);
            }
        });

        
		 

        stage.addActor(label);
        stage.addActor(newGameButton);
        stage.addActor(continueButton);
        //stage.addActor(settingsButton);
        stage.addActor(levelButton);
		 
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void draw() {
		sprite.setColor(1, 1, 0, 1);
		
		spriteBatch.begin();
		sprite.draw(spriteBatch);
		spriteBatch.end();
		
		stage.act();
		
		spriteBatch.begin();
		buttonFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		sprite.draw(spriteBatch);
		stage.draw();
		spriteBatch.end();
	}

	@Override
	public void handleInput() {
		
	}

	@Override
	public void dispose() {
		texture.dispose();
		buttonFont.dispose();
		titleFont.dispose();
		spriteBatch.dispose();
		stage.dispose();
		System.gc();
	}

}
