package com.bbaird.colorbeam.states;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.Save;

public class LevelWonState extends GameState {
	
	private Stage stage;
	
	private TextButton tryAgain;
	private TextButton nextLevel;
	private Label moves;
	private Random random;
	
	private int starCount;
	private int moveCount;
	
	private float timer;
	private float randomTimer;
	private float scale;
	
	private boolean isZoomingStar1;
	private boolean isZoomingStar2;
	private boolean isZoomingStar3;
	private boolean zoomFinishedStar1;
	private boolean zoomFinishedStar2;
	private boolean zoomFinishedStar3;
	private boolean showButtons;
	private boolean runGarbageCollector;
	
	
	private SpriteBatch tempBatch;
	
	
	private static final float WAIT = .2f; // normally 1.2f I liked
	
	
	
	public LevelWonState(GameStateManager gsm) {
		super(gsm);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(backButtonAdapter);
        Gdx.input.setInputProcessor(inputMultiplexer);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		stage = new Stage();
		randomTimer = 0;
		timer = 0;
		
		
		runGarbageCollector = true;
		tempBatch = new SpriteBatch();
		
		
		
		random = new Random();
		
		isZoomingStar1 = false;
		isZoomingStar2 = false;
		isZoomingStar3 = false;
		zoomFinishedStar1 = false;
		zoomFinishedStar2 = false;
		zoomFinishedStar3 = false;
		showButtons = false;
		

		
		//ColorBeam.background = new Image(textureRegion);
		ColorBeam.background.setBounds(0, 0, ColorBeam.WIDTH, ColorBeam.HEIGHT);
		
		Gdx.input.setInputProcessor(stage);
		//Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		

		
		starCount = Save.ad.getStars()[currentSection-1][currentLevel-1];
		moveCount = Save.ad.getMoves()[currentSection-1][currentLevel-1];
				
		moves = new Label("Moves: " + moveCount, ColorBeam.labelStyle);
		moves.setX(ColorBeam.WIDTH/2 - moves.getWidth()/2);
		moves.setY(ColorBeam.star2.getY() + ColorBeam.star2.getHeight() + moves.getHeight());
		

		
		nextLevel = new TextButton("Continue", ColorBeam.buttonStyle);
		nextLevel.setX(ColorBeam.WIDTH/2 - nextLevel.getWidth()/2);
		nextLevel.setY(2 * nextLevel.getHeight());
		nextLevel.setVisible(false);
		nextLevel.addListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
    			Save.save();
    			gsm.setState(GameStateManager.PLAY);

            }
        });
		
		tryAgain = new TextButton("Try Again", ColorBeam.buttonStyle);
		tryAgain.setX(ColorBeam.WIDTH/2 - tryAgain.getWidth()/2);
		tryAgain.setY(nextLevel.getY() + 2 * tryAgain.getHeight());
		tryAgain.setVisible(false);
		tryAgain.addListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {			    			
    			if (currentLevel == 1) {
    				currentLevel = 9;
    				currentSection--;
    		    	Save.ad.setCurrentLevel(currentLevel);
    		    	Save.ad.setCurrentSection(currentSection);
    			}
    			else Save.ad.setCurrentLevel(--currentLevel);
    			Save.save();
    			gsm.setState(GameStateManager.PLAY);

            }
        });

		
		ColorBeam.star1.setColor(Color.BLACK);
		ColorBeam.star2.setColor(Color.BLACK);
		ColorBeam.star3.setColor(Color.BLACK);
		

		//stage.addActor(ColorBeam.background);
		stage.addActor(moves);
		stage.addActor(ColorBeam.star1);
		stage.addActor(ColorBeam.star2);
		stage.addActor(ColorBeam.star3);
		stage.addActor(tryAgain);
		stage.addActor(nextLevel);
		
		// Update level and section
		if (currentLevel == 9) {
			currentLevel = 1;
			currentSection++;
		}
		else currentLevel++;
    	Save.ad.setCurrentLevel(currentLevel);
    	Save.ad.setCurrentSection(currentSection);
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		timer += dt;
		randomTimer += dt;
		if (timer > WAIT) {
			
			if (!zoomFinishedStar1) {
				if (isZoomingStar1) {
					if (scale > 1) scale /= 1.08f;
					else {
						if (starCount == 1) showButtons = true;
						zoomFinishedStar1 = true;
						timer = WAIT * .75f;
					}
				}
				else {
					ColorBeam.star1.setColor(Color.WHITE);
					isZoomingStar1 = true;
					scale = 10;
				}
				ColorBeam.star1.setScale(scale);
			}
			else if (!zoomFinishedStar2 && starCount >= 2) {
				if (isZoomingStar2) {
					if (scale > 1) scale /= 1.08f;
					else {
						if (starCount == 2) showButtons = true;
						zoomFinishedStar2 = true;
						timer = WAIT * .75f;
					}
				}
				else {
					ColorBeam.star2.setColor(Color.WHITE);
					isZoomingStar2 = true;
					scale = 10;
				}
				ColorBeam.star2.setScale(scale);
			}
			else if (!zoomFinishedStar3 && starCount >= 3) {
				if (isZoomingStar3) {
					if (scale > 1) scale /= 1.08f;
					else {
						if (starCount == 3) showButtons = true;
						zoomFinishedStar3 = true;
						timer = WAIT * .75f;
					}
				}
				else {
					ColorBeam.star3.setColor(Color.WHITE);
					isZoomingStar3 = true;
					scale = 10;
				}
				ColorBeam.star3.setScale(scale);
			}
		}
		// Button delay
		if (timer > WAIT && showButtons) {
			showButtons();
			if (runGarbageCollector) {
				System.gc();
				runGarbageCollector = false;
			}
		}
		
		// Random number
		if (randomTimer < WAIT * .75f) {
			int number = random.nextInt(10);
			moves.setText("Moves: " + number);
		}
		else moves.setText("Moves: " + moveCount);
		
		stage.act();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		tempBatch.begin();
		ColorBeam.background.draw(tempBatch);
		tempBatch.end();
		stage.draw();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}
	
	public void showButtons() {
		tryAgain.setVisible(true);
		nextLevel.setVisible(true);
	}

}

