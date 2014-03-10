package com.me.cyberPunkJam;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private VertigoRaiderGame vgr;
	public Terminal term;
	private AgentAI aai;

	private Sound badType;
	private Sound goodType;
	private Sound newLineSound;

	//private Music gameTheme;
	private Music alarmSound;

	//TextureAtlas atlas;
	TextureRegion buildingRegionStart;
	float buildingRegionStartX = 0;
	float buildingRegionStartY = 0;
	Point startCorner;

	TextureRegion buildingRegionEnd;
	float buildingRegionEndX = 0;
	float buildingRegionEndY = 0;
	Point endCorner;

	TextureRegion backgroundRegion;
	float backgroundRegionX = 0;
	float backgroundRegionY = 0;

	TextureRegion sequencerBackground;
	float sequencerBackgroundX = 0;
	float sequencerBackgroundY= 0;

	Hero hero;

	int numOfSignsLeft = 10;
	int numOfSignsRight = 10;

	Array<Sign> signsLeft;
	Array<Sign> signsRight;

	int numOfAntennaLeft = 5;
	int numOfAntennaRight = 5;

	Array<Antenna> antennaLeft;
	Array<Antenna> antennaRight;

	int numOfCitizens = 100;
	Array<Citizen> citizens;
	long lastCitizenTime;

	int numOfClouds = 10;
	Array<Cloud> clouds;

	int numOfCars = 4;
	Array<Car> cars;

	TextureRegion ropeRegion;

	int currentLevel = 0;
	Random ran = new Random();


	public GameScreen(final VertigoRaiderGame vgr) 
	{
		this.vgr = vgr;
		this.currentLevel = vgr.currentLevel;
		this.batch = vgr.batch;
		this.camera = new OrthographicCamera();
		Gdx.input.setInputProcessor(this);
		
		//vgr.currentMusic.stop();
		

		vgr.font.setColor(Color.GREEN);
		vgr.font.setScale(1.0f);

		//LOADING SOUND
		goodType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/type.wav"));
		badType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hit.wav"));
		newLineSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/newline.wav"));


		//LOADING MUSIC
//		gameTheme = Gdx.audio.newMusic(Gdx.files.internal("Sound/Music/TechnoTheme_ALPHA_01.ogg"));
//		gameTheme.setLooping(true);
		alarmSound = Gdx.audio.newMusic(Gdx.files.internal("Sound/FX/alarm.wav"));
		alarmSound.setLooping(true);

		//buildings
		buildingRegionStart = vgr.atlas.findRegion("building_ALPHA");
		buildingRegionEnd = vgr.atlas.findRegion("building_ALPHA");

		//background
		backgroundRegion = vgr.atlas.findRegion("background_ALPHA");

		//setting building locations
		buildingRegionStartX = 0;
		buildingRegionStartY = 0;

		buildingRegionEndX = vgr.VIRTUAL_WIDTH - buildingRegionEnd.getRegionWidth();
		buildingRegionEndY = 0;

		//Sequencer background
		sequencerBackground = vgr.atlas.findRegion("sequencerBackground");
		sequencerBackgroundX = vgr.VIRTUAL_WIDTH/4;
		sequencerBackgroundY = vgr.VIRTUAL_HEIGHT/4;

		/**
		 * Corner points are the starting and ending points for the hero. 
		 * Start Point: Where the hero starts
		 * End Point: Where the hero needs to cross to win.
		 */
		//Setting up corner points
		startCorner = new Point();
		endCorner = new Point();
		startCorner.setLocation(buildingRegionStartX + buildingRegionStart.getRegionWidth(), 
				(vgr.VIRTUAL_HEIGHT - (vgr.VIRTUAL_HEIGHT - buildingRegionStart.getRegionHeight())));
		endCorner.setLocation(buildingRegionEndX, 
				(vgr.VIRTUAL_HEIGHT - (vgr.VIRTUAL_HEIGHT - buildingRegionEnd.getRegionHeight())));

		//signs
		signsLeft = new Array<Sign>();
		for(int i = 0; i < numOfSignsLeft; i++)
		{
			signsLeft.add(new Sign(vgr.atlas, betweenTwo(startCorner.x - 400, startCorner.x), //200 = sign width
					betweenTwo(0, startCorner.y - 100)));
		}

		signsRight = new Array<Sign>();
		for(int i = 0; i < numOfSignsRight; i++)
		{
			signsRight.add(new Sign(vgr.atlas, betweenTwo(endCorner.x, endCorner.x + 200), //200 = sign width
					betweenTwo(0, endCorner.y - 100)));
		}

		//antenna
		antennaLeft = new Array<Antenna>();
		for(int i = 0; i < numOfAntennaLeft; i++)
		{
			antennaLeft.add(new Antenna(vgr.atlas, betweenTwo(startCorner.x - 400, startCorner.x - 300), //200 = sign width
					startCorner.y));
		}

		antennaRight = new Array<Antenna>();
		for(int i = 0; i < numOfAntennaRight; i++)
		{
			antennaRight.add(new Antenna(vgr.atlas, betweenTwo(endCorner.x, endCorner.x + 300), //200 = sign width
					endCorner.y));
		}

		//citizens
		citizens = new Array<Citizen>();
		for(int i = 0; i < numOfCitizens; i++)
		{
			spawnCitizen();
		}

		//clouds
		clouds = new Array<Cloud>();
		for(int i = 0; i < numOfClouds; i++)
		{
			spawnCloud();
		}

		//cars
		cars = new Array<Car>();
		for(int i = 0; i < numOfCars; i++)
		{
			spawnCar();
		}

		//Loading Terminal
		term = new Terminal(this.currentLevel);

		//Loading Agent AI
		aai = new AgentAI();

		hero = new Hero(vgr.atlas, startCorner.x, startCorner.y);

		//rope
		ropeRegion = vgr.atlas.findRegion("rope_ALPHA");
		//first get the text via Online
		term.textFromURL = term.ReadTextFromURL();
		//then process Text
		term.processText(term.textFromURL);
		//calculate distance hero needs to cross
		term.calculateHeroTravel(startCorner, endCorner);


	}

	private void spawnCloud() 
	{
		clouds.add(new Cloud(vgr.atlas, vgr.VIRTUAL_WIDTH, vgr.VIRTUAL_HEIGHT));		
	}

	private void spawnCar() 
	{
		cars.add(new Car(vgr.atlas, vgr.VIRTUAL_WIDTH, vgr.VIRTUAL_HEIGHT));
	}

	public void spawnCitizen()
	{
		citizens.add(new Citizen(vgr.atlas, vgr.VIRTUAL_WIDTH));
	}


	@Override
	public void render(float delta) 
	{

		camera.update();
		camera.apply(Gdx.gl10);

		//set view port
		Gdx.gl.glViewport((int) vgr.viewport.x, (int) vgr.viewport.y, 
				(int) vgr.viewport.width, (int) vgr.viewport.height);

		Gdx.gl.glClearColor(0, 0, 0.0f, 1); // 1 Alpha = no transparency
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//UPDATE LOGIC
		term.percentageComplete = (float)Math.round((term.numOfCharactersTypedCorrectly/term.numOfCharactersInText) * 10000) / 100;
		term.updateTimer();
		aai.update();
		if(aai.agentAIpercentage >= 80 && !alarmSound.isPlaying())
		{
			alarmSound.play();
		}
		else if(aai.agentAIpercentage < 80)
		{
			alarmSound.stop();
		}

		//check if the player is typing too slow
		aai.tooSlowImpulse(aai.tooSlowDeduction, term.updateTooSlowImpulse((int)term.timeLeft, aai.tooSlowThreshold));

		checkWinCondition();
		checkGameOverCondition();

		//UPDATE LOCATIONS
		hero.heroAnimatedSprite.setX(hero.heroX);
		hero.heroAnimatedSprite.setY(hero.heroY);
		sequencerBackgroundX = vgr.VIRTUAL_WIDTH/4;
		sequencerBackgroundY = vgr.VIRTUAL_HEIGHT/4;


		for(Sign s : signsLeft)
		{
			s.signAnimatedSprite.setPosition(s.x, s.y);
		}
		for(Sign s : signsRight)
		{
			s.signAnimatedSprite.setPosition(s.x, s.y);
		}

		for(Antenna a : antennaLeft)
		{
			a.antennaAnimatedSprite.setPosition(a.x, a.y);
		}
		for(Antenna a : antennaRight)
		{
			a.antennaAnimatedSprite.setPosition(a.x, a.y);
		}

		
		//INPUT LOGIC
		batch.begin();

		//resize background
		batch.draw(backgroundRegion, backgroundRegionX, backgroundRegionY);
		
		//draw rope
		batch.draw(ropeRegion, startCorner.x, startCorner.y  - 25, endCorner.x - startCorner.x, 25);

		//draw cars in the foreground
		for(Car c : cars)
		{
			c.moveCar();
			c.carAnimatedSprite.draw(batch);
			c.checkOffScreen(vgr.VIRTUAL_WIDTH, vgr.VIRTUAL_HEIGHT);
		}

		//draw buildings
		batch.draw(buildingRegionStart, buildingRegionStartX, buildingRegionStartY);
		batch.draw(buildingRegionEnd, buildingRegionEndX, buildingRegionEndY);

		//draw signs
		for(Sign s : signsLeft)
		{
			s.signAnimatedSprite.draw(batch);
		}
		for(Sign s : signsRight)
		{
			s.signAnimatedSprite.draw(batch);
		}

		for(Antenna a : antennaLeft)
		{
			a.antennaAnimatedSprite.draw(batch);
		}
		for(Antenna a : antennaRight)
		{
			a.antennaAnimatedSprite.draw(batch);
		}



		hero.heroAnimatedSprite.draw(batch);

		for(Citizen c : citizens)
		{
			c.moveCitizen();
			c.citizenAnimatedSprite.draw(batch);
			c.checkOffScreen(vgr.VIRTUAL_WIDTH, vgr.VIRTUAL_HEIGHT);
		}

		for(Cloud c : clouds)
		{
			c.moveCloud();
			c.cloudAnimatedSprite.draw(batch);
			c.checkOffScreen(vgr.VIRTUAL_WIDTH, vgr.VIRTUAL_HEIGHT);
		}

		vgr.font.setScale(1f);

		vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));

		vgr.font.setScale(2.0f);

		//required typing
		vgr.font.draw(batch, "anon$: " + term.currentLine, vgr.VIRTUAL_WIDTH/4, 740);
		//completed typing
		vgr.font.setColor(Color.GREEN);
		vgr.font.draw(batch, term.completedLine, vgr.VIRTUAL_WIDTH/4 + 96, 740);
		vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));

		vgr.font.setScale(2.0f);

		//Percentage complete in the game
		vgr.font.draw(batch, "PROGRESS: " + term.percentageComplete + "%", 50, 700);

		//Percentage close to losing to the Agent AI
		vgr.font.setColor((aai.agentAIpercentage/100f), 1f - (aai.agentAIpercentage/100f), 0f, 1f);
		vgr.font.draw(batch, "ALERT: " + Math.round(aai.agentAIpercentage)+ "%", 50, 740);
		vgr.font.setColor(Color.WHITE);

		//timer
		vgr.font.draw(batch, "TIMER: " + String.valueOf(Math.round(term.timeLeft))+ " secs", 50, 660);

		//sequencer
		if(term.isSequenceMode == true)
		{
			batch.draw(sequencerBackground, sequencerBackgroundX, sequencerBackgroundY, 
					vgr.VIRTUAL_WIDTH / 1.5f, vgr.VIRTUAL_HEIGHT / 1.5f);

			vgr.font.setScale(4.0f);
			vgr.font.setColor(Color.rgba8888(0.9f, 0.9f,  ran.nextFloat(), 1));
			vgr.font.draw(batch, "RANDOM HACK BELOW!", vgr.VIRTUAL_WIDTH/2 - 200, 600);

			vgr.font.setScale(6.0f);
			//required typing
			vgr.font.draw(batch, term.sequence, vgr.VIRTUAL_WIDTH/2 - 100, 400);
			//completed typing
			vgr.font.setColor(Color.GREEN);
			vgr.font.draw(batch, term.completedLineSequencer, vgr.VIRTUAL_WIDTH/2 - 100, 400);
			vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));

			vgr.font.setScale(2.0f);
		}

		//reset color
		vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));;

		vgr.font.setScale(1.0f);

		batch.end();


	}

	private void checkGameOverCondition() 
	{
		if(aai.agentAIpercentage >= 100 || term.timeLeft < 0)
		{
			vgr.accuracy = 0;
			vgr.points = 0;
			vgr.setScreen(new GameOverScreen(vgr));
			this.dispose();
		}
	}

	private void checkWinCondition()
	{
		if(term.percentageComplete >= 100)
		{
			
			if(vgr.currentLevel >= term.levels.size - 1)
			{
				vgr.accuracy = 0;
				vgr.points = 0;
				vgr.setScreen(new WinScreen(vgr));
			}
			
			//if not, just continue as usual
			else
			{
				vgr.accuracy = term.accuracy;
				vgr.points = term.points;
				vgr.setScreen(new TransitionScreen(vgr));
			}
			this.dispose();

		}
	}


	@Override
	public void resize(int width, int height) 
	{
		vgr.resize(width, height);
	}

	@Override
	public void show() 
	{
		vgr.currentMusic.stop();
		vgr.currentMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/Music/TechnoTheme_ALPHA_01.ogg"));
		vgr.currentMusic.setLooping(true);
		vgr.currentMusic.play();
		term.startTimer = System.nanoTime();

	}

	@Override
	public void hide() 
	{
		//gameTheme.stop();

	}

	@Override
	public void pause() 
	{


	}

	@Override
	public void resume() 
	{
		//gameTheme.play();

	}

	@Override
	public void dispose() 
	{
		//gameTheme.dispose();

		//disposing sound
		goodType.dispose();
		badType.dispose();
		alarmSound.dispose();



	}

	@Override
	public boolean keyDown(int keycode) 
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) 
	{

		//If it's a valid keycode
		if(character != 0)
		{
			term.currentTypedCharacter = character;
			//If we are not in Sequencer Mode
			if(term.isSequenceMode == false)
			{
				//If player typed the correct letter
				if(term.currentTypedCharacter == term.currentCharacter)
				{
					goodType.play(0.3f);
					moveHero();
					term.completedLine += character;
					term.numOfCorrectCharactersTyped++;
					term.numOfCharactersTypedCorrectly++;
					term.numOfCharactersTypedTotal++;

					//if there are still some characters to type on the line
					if(term.currentCharacterPointer + 1 < term.sizeOfCurrentLineArray)
					{
						term.currentCharacterPointer++;
						term.currentCharacter = term.currentLineArray[term.currentCharacterPointer];
					}
					//else if we've reached the end of the line
					else
					{
						newLineSound.play();
						term.currentCharacterPointer = 0;
						term.processNextLine();
						term.currentCharacter = term.currentLineArray[term.currentCharacterPointer];
					}
				}

				//If player typed the incorrect letter
				else
				{
					badType.play(0.3f);
					aai.wrongKeyImpulse(3f);
					term.numOfCharactersTypedIncorrectly++;
					term.numOfCharactersTypedTotal++;
				}
			}

			//If we are in Sequencer Mode
			else
			{
				if(term.currentTypedCharacter == term.currentSequencerCharacter)
				{
					goodType.play();
					term.completedLineSequencer += character;
					term.numOfCorrectCharactersTyped++;

					//if there are still some sequencer characters to type on the line
					if(term.currentCharacterPointerSequencer + 1 < term.sizeOfCurrentLineArraySequencer)
					{
						term.currentCharacterPointerSequencer++;
						term.currentSequencerCharacter = term.currentLineArraySequencer[term.currentCharacterPointerSequencer];
					}

					//else if we've reached the end of the sequencer
					else
					{
						newLineSound.play();
						term.generateSequence();
					}

				}

				//if the player typed the incorrect sequencer input
				else
				{
					badType.play(0.3f);
					aai.wrongKeyImpulse(3f);
				}
			}
		}
		return false;
	}

	private void moveHero() 
	{
		hero.heroX += term.pixelsPerMove;
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


	public void resetLevel() 
	{
		aai.reset();
		currentLevel = 0;
		term.reset(startCorner, endCorner);
		hero.reset(startCorner);


	}

	public void nextLevel()
	{
		System.out.println("nextlevel Gamescreen run");
		aai.reset();
		term.nextLevel(startCorner, endCorner);
		hero.reset(startCorner);

	}

	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}



}
