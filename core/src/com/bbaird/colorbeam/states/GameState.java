package com.bbaird.colorbeam.states;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bbaird.colorbeam.ColorBeam;
import com.bbaird.colorbeam.managers.GameStateManager;
import com.bbaird.colorbeam.managers.Save;

public abstract class GameState {
	protected GameStateManager gsm;
	
	protected int currentLevel;
	protected int currentSection;
	
	protected InputAdapter backButtonAdapter;
	
	protected boolean additive;
	
	protected Texture[] textures;
	protected TextureRegion textureRegion;
	
	protected GameState(GameStateManager mygsm) {
		this.gsm = mygsm;
		
		currentLevel = Save.ad.getCurrentLevel();
		currentSection = Save.ad.getCurrentSection();
        
		textures = new Texture[Save.ad.getTotalSections()];
		for (int i = 1; i < Save.ad.getTotalSections() + 1; i++) {
			Texture t = new Texture("images/texture" + i + ".jpg");
			t.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);
			textures[i-1] = t;
		}
		
		textureRegion = new TextureRegion(textures[currentSection - 1], ColorBeam.WIDTH, ColorBeam.HEIGHT);
		additive = Save.ad.getMixType();
		
		backButtonAdapter = new InputAdapter() {
            public boolean keyDown(int keycode) {
                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK))
                	gsm.setState(GameStateManager.MENU);
                return false;
            }
        };
		
		init();


	}
	
	public abstract void init();
	public abstract void update(float dt);
	public abstract void draw();
	public abstract void handleInput();
	public abstract void dispose();
}
