package com.me.cyberPunkJam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class VertigoRaiderGame extends Game {
	private OrthographicCamera camera;
	SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	BitmapFont font;
	private com.me.cyberPunkJam.MainMenuScreen mainMenuScreen;
	private com.me.cyberPunkJam.GameScreen gameScreen;
	private com.me.cyberPunkJam.GameOverScreen gameOverScreen;
	float w = 0;
	float h = 0;
	
	public void create() 
	{		
		this.w = Gdx.graphics.getWidth();
		this.h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		//use libgdx's default Arial font.
		font = new BitmapFont();
		
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		
		this.setScreen(gameScreen);
		
		

	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	public void render() 
	{		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}


}
