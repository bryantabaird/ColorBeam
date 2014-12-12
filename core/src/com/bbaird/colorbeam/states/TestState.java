/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.bbaird.colorbeam.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bbaird.colorbeam.managers.GameStateManager;

public class TestState extends GameState implements AssetErrorListener {
	public TestState(GameStateManager mygsm) {
		super(mygsm);
		// TODO Auto-generated constructor stub
	}

	AssetManager manager;
	SpriteBatch batch;
	int frame = 0;
	int reloads = 0;
	float elapsed = 0;

	public void create () {
		Gdx.app.setLogLevel(Application.LOG_ERROR);

		 Resolution[] resolutions = new Resolution[4];
         resolutions[0] = new Resolution(300, 500, "300x500");
         resolutions[1] = new Resolution(480, 800, "480x800");
         resolutions[2] = new Resolution(768, 1280, "768x1280");
         resolutions[3] = new Resolution(1152, 1920, "1152x1920");
		ResolutionFileResolver resolver = new ResolutionFileResolver(new InternalFileHandleResolver(), resolutions);
		manager = new AssetManager();
		manager.setLoader(Texture.class, new TextureLoader(resolver));
		manager.setErrorListener(this);
		load();
		Texture.setAssetManager(manager);
		batch = new SpriteBatch();
		
		System.out.println(" width: " + Gdx.graphics.getWidth());
		System.out.println("height: " + Gdx.graphics.getHeight());
		System.out.println("density: " + Gdx.graphics.getDensity());
	}

	boolean diagnosed = false;
	//private long start;
	private Texture tex1;

	private void load () {
		//start = TimeUtils.nanoTime();
		tex1 = new Texture("images/star.png");
		

		//start = TimeUtils.nanoTime();
		manager.load("images/star.png", Texture.class);
	}

	private void unload () {
		tex1.dispose();

		manager.unload("images/star.png");
	}

	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		boolean result = manager.update();
		if (result) {
			if (!diagnosed) {
				diagnosed = true;
				
				elapsed = 0;
			} else {
				elapsed += Gdx.graphics.getRawDeltaTime();
				if (elapsed > 0.2f) {
					unload();
					load();
					diagnosed = false;
					reloads++;
				}
			}
		}
		frame++;

		batch.begin();
		if (manager.isLoaded("images/star.png")) batch.draw(manager.get("images/star.png", Texture.class), 100, 100);
		
		batch.end();

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error (AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error("AssetManagerTest", "Couldn't load asset: " + asset, (Exception)throwable);
	}

	@Override
	public void dispose () {
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		create();
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		render();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
}