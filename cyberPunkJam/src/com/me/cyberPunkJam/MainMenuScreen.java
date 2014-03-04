package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen implements Screen 
{
	SpriteBatch batch;
	VertigoRaiderGame vrg;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Random ran = new Random();
	private Vector2 mousePos;
	
	Button playButton;
	Button creditsButton;
	
	public MainMenuScreen(final VertigoRaiderGame vrg) 
	{
		this.vrg = vrg;
		this.batch = vrg.batch;
		this.font = vrg.font;
		
		this.camera = new OrthographicCamera();
		font.setScale(5f);
		
		mousePos = new Vector2();
		
		playButton = new Button(vrg.atlas.findRegion("play"), 300, 300);
		creditsButton = new Button(vrg.atlas.findRegion("credits"), 700, 300);
		
	}

	@Override
	public void render(float delta) 
	{
	
		camera.update();
		camera.apply(Gdx.gl10);

		//set view port
		Gdx.gl.glViewport((int) vrg.viewport.x, (int) vrg.viewport.y, 
				(int) vrg.viewport.width, (int) vrg.viewport.height);

		Gdx.gl.glClearColor(0, 0, 0.0f, 1); // 1 Alpha = no transparency
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//update
		mousePos.set(Gdx.input.getX(), Gdx.input.getY());
		
		if(playButton.isClicked(mousePos, Gdx.input.isButtonPressed(0)))
		{
			vrg.setScreen(vrg.gameScreen);
			this.hide();
		}
		
		
		//batch
		batch.begin();
		
		font.setScale(5f);
		font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));
		font.draw(batch, "VERTIGO RAIDERS", vrg.VIRTUAL_WIDTH/4, 600);
		
		batch.draw(playButton.texture, playButton.buttonX, playButton.buttonY);
		batch.draw(creditsButton.texture, creditsButton.buttonX, creditsButton.buttonY);

		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
