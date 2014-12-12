package com.bbaird.colorbeam.managers;

import com.bbaird.colorbeam.states.GameState;
import com.bbaird.colorbeam.states.LevelState;
import com.bbaird.colorbeam.states.LevelWonState;
import com.bbaird.colorbeam.states.MenuState;
import com.bbaird.colorbeam.states.PlayState;
import com.bbaird.colorbeam.states.SettingsState;
import com.bbaird.colorbeam.states.TestState;

public class GameStateManager {
	//current game state
	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int SETTINGS = 2;
	public static final int LEVELS = 3;
	public static final int LEVELWON = 4;
	public static final int TEST = 5;

	
	
	public GameStateManager() {
		setState(MENU);
	}
	
	
	
	public void setState(int state) {
		if (gameState != null) gameState.dispose();
		
		if (state == MENU) {
			gameState = new MenuState(this);
		}
		if (state == PLAY) {
			gameState = new PlayState(this);
		}
		if (state == SETTINGS) {
			gameState = new SettingsState(this);
		}		
		if (state == LEVELS) {
			gameState = new LevelState(this);
		}
		if (state == LEVELWON) {
			gameState = new LevelWonState(this);
		}
		if (state == TEST) {
			gameState = new TestState(this);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {
		gameState.draw();
	}

	public void dispose() {
		gameState.dispose();

	}
}