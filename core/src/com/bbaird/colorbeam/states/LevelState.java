package com.bbaird.colorbeam.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.Save;

public class LevelState extends GameState {

	private TextureRegion textureRegion;
	private Texture smallStar;
	private Texture triangleTexture;
	private BitmapFont buttonFont;
	private BitmapFont titleFont;
	private SpriteBatch spriteBatch;
	private LabelStyle labelStyle;
	private Image background;
	private Stage stage;
	private Label label;
	
	private float fontWidth;
	private float fontHeight;
	private int displaySection;
	private int height;
	private int width;
	
	private InputListener nextSectionListener;
	private InputListener prevSectionListener;
	
	private final String TITLE = "Levels";
		
	public LevelState(GameStateManager gsm) {
		super(gsm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() {		
		height = ColorBeam.HEIGHT;
		width = ColorBeam.WIDTH;
		displaySection = Save.ad.getCurrentSection();
		
		stage = new Stage();
		textureRegion = new TextureRegion(textures[displaySection-1], width, height);
		background = new Image(textureRegion);
//		buttonFont = new BitmapFont(Gdx.files.internal("fonts/" + FONTNAME + ".fnt"), 
//				Gdx.files.internal("fonts/" + FONTNAME + ".png"), false);
//		titleFont = new BitmapFont(Gdx.files.internal("fonts/" + TITLEFONTNAME + ".fnt"), 
//				Gdx.files.internal("fonts/" + TITLEFONTNAME + ".png"), false);
		labelStyle = new LabelStyle();
		spriteBatch = new SpriteBatch();
		smallStar = new Texture("images/star.png");
		triangleTexture = new Texture("images/triangle.png");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AnimeAceItalic.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (48 * Gdx.graphics.getDensity());
		buttonFont = generator.generateFont(parameter);
		parameter.size = (int) (64 * Gdx.graphics.getDensity());
		titleFont = generator.generateFont(parameter);
		generator.dispose();
		
		
		labelStyle.font = titleFont;
		fontWidth = titleFont.getBounds(TITLE).width;
		fontHeight = titleFont.getBounds(TITLE).height;
		
		label = new Label(TITLE, labelStyle);
		label.setPosition(width/2 - fontWidth/2, 7*height/8);
		label.setWidth(fontWidth);
		label.setHeight(fontHeight);

		
		
		// Handles next section listeners
		nextSectionListener = new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	displaySection++;
            	displaySection();

            }
        };
        
		prevSectionListener = new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	displaySection--;
            	displaySection();

            }
        };
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(backButtonAdapter);
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        displaySection();
	}
	
	public void addStaticActors() {
		textureRegion.setTexture(textures[displaySection-1]);
		background = new Image(textureRegion);
		stage.addActor(background);
		stage.addActor(label);	
		if (displaySection != 1) {
			TextButtonStyle bStyle = new TextButtonStyle();
			fontWidth = titleFont.getBounds("<").width;
			fontHeight = titleFont.getBounds("<").height;
			bStyle.font = titleFont;
			TextButton prevButton = new TextButton("<", bStyle);
			prevButton.setBounds((label.getX() - fontWidth)/2, 7*height/8, fontWidth, fontHeight);
			prevButton.addListener(prevSectionListener);
			stage.addActor(prevButton);
		}
		
		if (displaySection != Save.ad.getTotalSections()) {
			TextButtonStyle bStyle = new TextButtonStyle();
			fontWidth = titleFont.getBounds(">").width;
			fontHeight = titleFont.getBounds(">").height;
			bStyle.font = titleFont;
			TextButton nextButton = new TextButton(">", bStyle);
			nextButton.setBounds(label.getX() + label.getWidth() + (width - label.getX() - label.getWidth() - fontWidth)/2, 7*height/8, fontWidth, fontHeight);
			nextButton.addListener(nextSectionListener);
			stage.addActor(nextButton);
		}
	}
	
	public void displaySection() {
		stage.clear();
		addStaticActors();
		
		int[][] stars = Save.ad.getStars();
		int level = 0;
		for (int h = 3; h > 0; h--) {
			for (int w = 1; w < 4; w++) {
				final int currentLevel = level + 1;
				Image triangle = new Image(triangleTexture);
				
				fontWidth = buttonFont.getBounds(Integer.toString(currentLevel)).width;
				fontHeight = buttonFont.getBounds(Integer.toString(currentLevel)).height;
				
				TextButtonStyle style = new TextButtonStyle();
				
				style.up = triangle.getDrawable();
				style.down = triangle.getDrawable();
				style.font = buttonFont;
				
				TextButton button = new TextButton(Integer.toString(currentLevel), style);
				
				button.setWidth(ColorBeam.WIDTH/5);
				button.setHeight(ColorBeam.WIDTH/5);
				
				float bWidth = button.getWidth();
				float bHeight = button.getHeight();
				
				InputListener startLevelListener = new InputListener() {
		        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		            	return true;
		            }
		            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		            	Save.ad.setCurrentLevel(currentLevel);
		            	Save.ad.setCurrentSection(displaySection);
		            	gsm.setState(GameStateManager.PLAY);

		            }
		        };
		       
				button.setPosition(w*width/4 - bWidth/2, h*height/4 - 2*bHeight/3);
				
				float starSide = bWidth/4;

				Image star1 = new Image(smallStar);
				star1.setBounds(w*width/4 - 7*starSide/4, h*height/4 - bHeight/2 - starSide, starSide, starSide);
				star1.setOrigin(starSide/2, starSide/2);
				Image star2 = new Image(smallStar);
				star2.setBounds(w*width/4 - starSide/2, h*height/4 - bHeight/2 - starSide, starSide, starSide);
				star2.setOrigin(starSide/2, starSide/2);
				Image star3 = new Image(smallStar);
				star3.setBounds(w*width/4 + 3*starSide/4, h*height/4 - bHeight/2 - starSide, starSide, starSide);
				star3.setOrigin(starSide/2, starSide/2);
				
				if (stars[displaySection-1][currentLevel-1] == 0) {
					star1.setColor(Color.GRAY);
					star2.setColor(Color.GRAY);
					star3.setColor(Color.GRAY);
				}
				else if (stars[displaySection-1][currentLevel-1] == 1) {
					star2.setColor(Color.GRAY);
					star3.setColor(Color.GRAY);
				}
				else if (stars[displaySection-1][currentLevel-1] == 2) {
					star3.setColor(Color.GRAY);
				}
				
				style.fontColor = Color.DARK_GRAY;
				if (displaySection == 1 && currentLevel == 1) {
					button.addListener(startLevelListener);
					style.fontColor = Color.WHITE;
				}
				else if (displaySection == 1) {
					if (Save.ad.getFinishedLevels()[displaySection-1][currentLevel-2] == true) { 
						button.addListener(startLevelListener);
						style.fontColor = Color.WHITE;
					}
				}
				else if (currentLevel == 1) {
					if (Save.ad.getFinishedLevels()[displaySection-2][Save.ad.getTotalLevels()-1] == true) { 
						button.addListener(startLevelListener);
						style.fontColor = Color.WHITE;
					}
				}
				else if (Save.ad.getFinishedLevels()[displaySection-1][currentLevel-2] == true) {
					button.addListener(startLevelListener);
					style.fontColor = Color.WHITE;
				}
		        stage.addActor(button);
		        stage.addActor(star1);
		        stage.addActor(star2);
		        stage.addActor(star3);
		        
		        
		        
		        level++;
			}
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		for (Actor a: stage.getActors()) {
			if (a.getClass()==com.badlogic.gdx.scenes.scene2d.ui.Image.class) {
				if (a != background) {
					a.setRotation(a.getRotation()-1);
				}
			}
		}
		stage.act();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
		stage.draw();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		buttonFont.dispose();
		spriteBatch.dispose();
		titleFont.dispose();
		stage.dispose();
		smallStar.dispose();
		triangleTexture.dispose();
		System.gc();
	}

}
