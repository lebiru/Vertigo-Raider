package com.me.cyberPunkJam;

import java.util.Random;

import tweens.ActorAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
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


/**
 * Credits to Dermetfan for providing most of this source code via YouTube/BitBucket.
 * The intro screen gives the backstory and how to play.
 * @author Biru
 *
 */
public class Intro implements Screen 
{
	SpriteBatch batch;
	VertigoRaiderGame vrg;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Random ran = new Random();
	private Vector2 mousePos;

	private Stage stage;
	private Skin skin;
	private Table table;
	private TweenManager tweenManager;

	public Intro(final VertigoRaiderGame vrg) 
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

		
	   if(Gdx.input.isKeyPressed(Keys.ESCAPE))
	   {
		   dispose();
		   vrg.setScreen(new Intro(vrg));
	   }

		
		batch.end();
		
		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
	
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void show() 
	{
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);

		// creating heading
		Label heading = new Label("THE STORY", skin, "big");
		heading.setFontScale(2);
		
		//creating the story
		Label lineOne = new Label("You've been hired to take down MEGACOM, an " +
				"international organ repo business that's done some VERY bad things.", skin, "small");
		Label lineTwo= new Label("As long as you type and hack quickly, you'll avoid suspicion from MEGACOM's henchmen.", skin, "small");
		Label lineThree = new Label("Fail, and you'll never see the grey skies above the ports again.", skin, "small");
		Label lineFour = new Label("Are you ready, cyber cowboy?", skin, "small");

		TextButton buttonPlay = new TextButton("START", skin, "default");
		buttonPlay.addListener(new ClickListener() 
		{

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Timeline.createParallel().beginParallel()
				.push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
				.push(Tween.to(table, ActorAccessor.Y, .75f).target(table.getY() - 50)
						.setCallback(new TweenCallback() {

							@Override
							public void onEvent(int type, BaseTween<?> source) 
							{
								dispose();
								vrg.setScreen(new GameScreen(vrg));
							}
						}))
						.end().start(tweenManager);
			}
		});
		buttonPlay.pad(15);

		// putting stuff together
		table.add(heading).spaceBottom(100).row();
		table.add(lineOne).spaceBottom(100).row();
		table.add(lineTwo).spaceBottom(100).row();
		table.add(lineThree).spaceBottom(100).row();
		table.add(lineFour).spaceBottom(100).row();
		table.add(buttonPlay);

		stage.addActor(table);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence().beginSequence()
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
		.end().repeat(Tween.INFINITY, 0).start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence()
		.push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
		.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.to(buttonPlay, ActorAccessor.ALPHA, .25f).target(1))
		.end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0).start(tweenManager);

		Tween.to(lineOne, ActorAccessor.Y, 2f).target(500f).delay(0f).start(tweenManager);
		Tween.to(lineTwo, ActorAccessor.Y, 2f).target(400f).delay(0f).start(tweenManager);
		Tween.to(lineThree, ActorAccessor.Y, 2f).target(300f).delay(0f).start(tweenManager);
		Tween.to(lineFour, ActorAccessor.Y, 2f).target(200f).delay(0f).start(tweenManager);
		Tween.to(buttonPlay, ActorAccessor.Y, 2f).target(100f).start(tweenManager);
		
		tweenManager.update(Gdx.graphics.getDeltaTime());

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
	public void dispose() 
	{
		stage.dispose();
		skin.dispose();

	}

}
