package com.me.cyberPunkJam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class VertigoRaiderGame extends Game {
	private OrthographicCamera camera;
	SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public TextureAtlas atlas;
	
	BitmapFont font;
	public com.me.cyberPunkJam.MainMenuScreen mainMenuScreen;
	public com.me.cyberPunkJam.GameScreen gameScreen;
	public com.me.cyberPunkJam.GameOverScreen gameOverScreen;
	public com.me.cyberPunkJam.TransitionScreen transitionScreen;
	static final int VIRTUAL_WIDTH = 1366;
	static final int VIRTUAL_HEIGHT = 786;
	private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
	Rectangle viewport;
	
	public void create() 
	{		
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		atlas = new TextureAtlas("Art/Atlas.txt");
		
		//use libgdx's default Arial font.
		font = new BitmapFont();
		
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		transitionScreen = new TransitionScreen(this);
		
		this.setScreen(mainMenuScreen);
		
		

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
	public void resize(int width, int height) 
	{
		//calculate new viewport; CREDITS TO http://www.acamara.es/blog/2012/02/
		//keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
		
		float aspectRatio = (float)width/(float)height;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);
		
		if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
		
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}


}
