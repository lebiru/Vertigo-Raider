package com.me.cyberPunkJam;

import java.util.Random;

import tweens.ActorAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen 
{
	SpriteBatch batch;
	VertigoRaiderGame vrg;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Random ran = new Random();
	private Vector2 mousePos;

	TextureRegion backgroundRegion;
	float backgroundRegionX = 0;
	float backgroundRegionY = 0;

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;

	//particle effects
	private ParticleEffect effect;

	public GameOverScreen(final VertigoRaiderGame vrg) 
	{
		this.vrg = vrg;
		this.batch = vrg.batch;
		this.font = vrg.font;

		this.camera = new OrthographicCamera();
		font.setScale(5f);

		mousePos = new Vector2();

		Texture.setEnforcePotImages(false);
	}

	@Override
	public void render(float delta) 
	{

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(backgroundRegion, backgroundRegionX, backgroundRegionY);


		effect.draw(batch, delta);

		batch.end();

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);

		if(effect.isComplete())
		{
			effect.reset();
		}

	}

	@Override
	public void resize(int width, int height) 
	{
		stage.setViewport(width, height, true);
		table.invalidateHierarchy();
		vrg.resize(width, height);
	}

	@Override
	public void show() 
	{
		
		//MUSIC
		vrg.currentMusic.stop();
		vrg.currentMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Music/GameOverTheme_ALPHA_00.ogg"));
		vrg.currentMusic.setLooping(true);
		vrg.currentMusic.play();
		
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);

		// creating heading
		Label heading = new Label("GAME OVER", skin, "big");
		heading.setFontScale(1.5f);
		
		Label lineOne = new Label("You couldn't hack fast enough...", skin, "default");

		// creating buttons
		TextButton buttonPlay = new TextButton("ONE MORE TIME", skin, "default");
		buttonPlay.addListener(new ClickListener() 
		{

			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				vrg.gameScreen.nextLevel();
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(vrg));
				dispose();
			}

		});
		buttonPlay.pad(15);

		// creating buttons
		TextButton buttonQuit = new TextButton("I GIVE UP", skin, "default");
		buttonQuit.addListener(new ClickListener() 
		{

			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				vrg.gameScreen.resetLevel();
				vrg.currentLevel = 0;
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(vrg));
				dispose();
			}

		});
		buttonQuit.pad(15);


		// putting stuff together
		table.add(heading).spaceBottom(100).row();
		table.add(lineOne).spaceBottom(20f).row();
		table.add(buttonPlay).spaceBottom(15).row();
		table.add(buttonQuit).spaceBottom(15).row();

		stage.addActor(table);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence().beginSequence()
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
		.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence()
		.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
		.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0).ease(TweenEquations.easeInBack))
		.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
		.push(Tween.to(buttonQuit, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

		//background
		backgroundRegion = vrg.atlas.findRegion("background_ALPHA");

		//particle effects
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/spark.p"), Gdx.files.internal("effects"));
		effect.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		effect.start();

	}

	@Override
	public void hide() {
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
	public void dispose() 
	{
		effect.dispose();
		stage.dispose();
		skin.dispose();
	}

}
