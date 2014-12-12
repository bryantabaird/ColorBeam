package com.bbaird.colorbeam;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.InputGestures;
import com.bbaird.colorbeam.managers.Save;

public class ColorBeam implements ApplicationListener {

	public static int WIDTH;
	public static int HEIGHT;
	//careful, may need to be floats
	
	public static OrthographicCamera cam;
	
	private GameStateManager gsm;
	
	
	
	//Shared among states
	public static Sprite background;
	
	
	//Play state
	public static Texture screw;
	public static Texture laser;
	public static Texture endLaser;
	public static Texture regularTriangle;
	public static Texture absorbentTriangle;
	public static Texture glassTriangle;
	public static Texture endPoint1;
	public static Texture endPoint2;
	public static Texture endPoint3;
	public static Texture endPoint4;
	public static Texture startPoint;
	public static Texture wormholeBack;
	public static Texture wormholeFrontA;
	public static Texture wormholeFrontB;
	public static Texture intersectPoint;
	
	//public static Sprite background;
	public static Sprite screwSprite;
	public static Sprite laserSprite;
	public static Sprite endLaserSprite;
	public static Sprite regularTriangleSprite;
	public static Sprite absorbentTriangleSprite;
	public static Sprite glassTriangleSprite;
	public static Sprite endPointSprite1;
	public static Sprite endPointSprite2;
	public static Sprite endPointSprite3;
	public static Sprite endPointSprite4;
	public static Sprite startPointSprite;
	public static Sprite wormholeBackSprite;
	public static Sprite wormholeFrontSpriteA;
	public static Sprite wormholeFrontSpriteB;
	public static Sprite intersectPointSprite;
	
	//Level Won State
	public static Image star1;
	public static Image star2;
	public static Image star3;
	
	public Texture star;
	
	public BitmapFont labelFont;
	public BitmapFont buttonFont;
	public static TextButtonStyle buttonStyle;
	public static LabelStyle labelStyle;
	
	private static int PADDING;	
	
	
	@Override
	public void create () {
		resume();
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH / 2, HEIGHT / 2);
		cam.update();
		
		gsm = new GameStateManager();
		
		Gdx.input.setCatchBackKey(true);
		load();
	}
	
	private void load() {
		loadPlayState();
		loadLevelWonState();
	}
	
	private void loadPlayState() {
		screw = new Texture("images/screw.png");
		laser = new Texture("images/laser.png");
		endLaser = new Texture("images/endlaser.png");
		regularTriangle = new Texture("images/triangle.png");
		absorbentTriangle = new Texture("images/absorbent.png");
		glassTriangle = new Texture("images/glass.png");
		endPoint1 = new Texture("images/endpoint.png");
		startPoint = new Texture("images/startpoint.png");
		wormholeBack = new Texture("images/wormholeBack.png");
		intersectPoint = new Texture("images/intersectpoint.png");
		
		screwSprite = new Sprite(screw);
		laserSprite = new Sprite(laser);
		endLaserSprite = new Sprite(endLaser);
		regularTriangleSprite = new Sprite(regularTriangle);
		absorbentTriangleSprite = new Sprite(absorbentTriangle);
		glassTriangleSprite = new Sprite(glassTriangle);
		endPointSprite1 = new Sprite(endPoint1);
		//endPointSprite2 = new Sprite(endPoint2);
		//endPointSprite3 = new Sprite(endPoint3);
		//endPointSprite4 = new Sprite(endPoint4);
		startPointSprite = new Sprite(startPoint);
		wormholeBackSprite = new Sprite(wormholeBack);
		//wormholeFrontSpriteA = new Sprite(wormholeFrontA);
		//wormholeFrontSpriteB = new Sprite(wormholeFrontB);
		intersectPointSprite = new Sprite(intersectPoint);
	}
	
	private void loadLevelWonState() {
		float starSide = ColorBeam.WIDTH/4;
		PADDING = ColorBeam.WIDTH / 50;
		star = new Texture("images/star.png");
		star1 = new Image(star);
		star1.setBounds(starSide/2 - PADDING, ColorBeam.HEIGHT/2, starSide, starSide);
		star1.setOrigin(starSide/2, starSide/2);
		star1.setColor(Color.BLACK);
		
		star2 = new Image(star);
		star2.setBounds(3*starSide/2, ColorBeam.HEIGHT/2, starSide, starSide);
		star2.setOrigin(starSide/2, starSide/2);
		star2.setColor(Color.BLACK);
		
		star3 = new Image(star);
		star3.setBounds(5*starSide/2 + PADDING, ColorBeam.HEIGHT/2, starSide, starSide);
		star3.setOrigin(starSide/2, starSide/2);
		star3.setColor(Color.BLACK);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AnimeAceItalic.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (64 * Gdx.graphics.getDensity());
		labelFont = generator.generateFont(parameter);
		parameter.size = (int) (48 * Gdx.graphics.getDensity());
		buttonFont = generator.generateFont(parameter);
		generator.dispose();
		
		buttonStyle = new TextButtonStyle();
		labelStyle = new LabelStyle();
		buttonStyle.font = buttonFont;
		labelStyle.font = labelFont;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		
		InputGestures.update();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		//System.out.println("paused!");
		Save.save();
	}

	@Override
	public void resume() {
		//System.out.println("resumed!");
		Save.load();
	}

	@Override
	public void dispose() {
		gsm.dispose();
		
		screw.dispose();
		laser.dispose();
		endLaser.dispose();
		regularTriangle.dispose();
		absorbentTriangle.dispose();
		glassTriangle.dispose();
		endPoint1.dispose();
		//endPoint2.dispose();
		//endPoint3.dispose();
		//endPoint4.dispose();
		startPoint.dispose();
		wormholeBack.dispose();
		//wormholeFrontA.dispose();
		//wormholeFrontB.dispose();
		intersectPoint.dispose();
	}
}
