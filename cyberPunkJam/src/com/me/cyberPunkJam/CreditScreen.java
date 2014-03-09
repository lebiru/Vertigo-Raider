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
 * @author Biru
 *
 */
public class CreditScreen implements Screen 
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

	public CreditScreen(final VertigoRaiderGame vrg) 
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
		Label heading = new Label("Vertigo Raiders", skin, "big");
		
		Label lineOne = new Label("Bilal | Programmer | @ninjabit6", skin, "default");
		Label lineTwo = new Label("Nico | Art |  www.nicotraut.com", skin, "default");
		Label lineThree = new Label("Theodore | Music | www.soundcloud.com/ttvgm", skin, "default");
		Label lineFour = new Label("itch.io #CyberPunkJam 2014", skin, "default");
		
		heading.setFontScale(2);

		// creating buttons
		TextButton buttonMain = new TextButton("Back", skin, "big");
		buttonMain.addListener(new ClickListener() 
		{

			@Override
			public void clicked(InputEvent event, float x, float y) 
			{
				dispose();
				((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(vrg));
			}

		});
		buttonMain.pad(15);


		// putting stuff together
		table.add(heading).spaceBottom(100).row();
		table.add(lineOne).spaceBottom(20).row();
		table.add(lineTwo).spaceBottom(20).row();
		table.add(lineThree).spaceBottom(20).row();
		table.add(lineFour).spaceTop(30f).row();
		table.add(buttonMain).spaceTop(30f).row();


		stage.addActor(table);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence().beginSequence()
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 0))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(0, 1, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 0, 1))
		.push(Tween.to(heading, ActorAccessor.RGB, .5f).target(1, 1, 1))
		.end().repeat(Tween.INFINITY, 0).start(tweenManager);
		
		Timeline.createSequence().beginSequence()
		.push(Tween.to(lineOne, ActorAccessor.Y, 1.0f).target(340f).ease(TweenEquations.easeInBounce))
		.end().start(tweenManager);
		
		Timeline.createSequence().beginSequence()
		.push(Tween.to(lineTwo, ActorAccessor.Y, 1.0f).target(300f).ease(TweenEquations.easeInBounce))
		.end().start(tweenManager);
		
		Timeline.createSequence().beginSequence()
		.push(Tween.to(lineThree, ActorAccessor.Y, 1.0f).target(260f).ease(TweenEquations.easeInBounce))
		.end().start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence()
		.push(Tween.set(buttonMain, ActorAccessor.ALPHA).target(0))
		.push(Tween.from(heading, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.from(lineOne, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.from(lineTwo, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.from(lineThree, ActorAccessor.ALPHA, .25f).target(0))
		.push(Tween.to(buttonMain, ActorAccessor.ALPHA, .25f).target(1))
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
