package com.me.cyberPunkJam;

import java.awt.Point;
import java.awt.event.KeyEvent;
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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen, InputProcessor
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private VertigoRaiderGame vgr;
	private Terminal term;
	private AgentAI aai;

	private Sound badType;
	private Sound goodType;
	private Sound newLineSound;

	private Music gameTheme;

	//TextureAtlas atlas;
	TextureRegion buildingRegionStart;
	float buildingRegionStartX = 0;
	float buildingRegionStartY = 0;
	Point startCorner = new Point();

	TextureRegion buildingRegionEnd;
	float buildingRegionEndX = 0;
	float buildingRegionEndY = 0;
	Point endCorner = new Point();

	Hero hero;
	
	Array<Citizen> citizens;
	long lastCitizenTime;
	
	public void spawnCitizen()
	{
		citizens.add(new Citizen(vgr.atlas));
		lastCitizenTime = TimeUtils.nanoTime();
	}

	TextureRegion agentRegion;
	Animation agentAnimation;
	AnimatedSprite agentAnimatedSprite;
	float agentX = 0;
	float agentY = 0;

	TextureRegion ropeRegion;

	Random ran = new Random();


	public GameScreen(final VertigoRaiderGame vgr) 
	{
		this.vgr = vgr;
		this.batch = vgr.batch;
		this.camera = new OrthographicCamera();
		Gdx.input.setInputProcessor(this);


		vgr.font.setColor(Color.GREEN);
		vgr.font.setScale(1.0f);

		//LOADING SOUND
		goodType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/type.wav"));
		badType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hit.wav"));
		newLineSound = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/newline.wav"));


		//LOADING MUSIC
		gameTheme = Gdx.audio.newMusic(Gdx.files.internal("Sound/Music/gameTheme.ogg"));
		gameTheme.setLooping(true);

		//buildings
		buildingRegionStart = vgr.atlas.findRegion("building");
		buildingRegionEnd = vgr.atlas.findRegion("building");

		//setting building locations
		buildingRegionStartX = 0;
		buildingRegionStartY = 0;

		buildingRegionEndX = vgr.VIRTUAL_WIDTH - buildingRegionEnd.getRegionWidth();
		buildingRegionEndY = 0;

		/**
		 * Corner points are the starting and ending points for the hero. 
		 * Start Point: Where the hero starts
		 * End Point: Where the hero needs to cross to win.
		 */
		//Setting up corner points
		startCorner.setLocation(buildingRegionStart.getRegionX() + buildingRegionStart.getRegionWidth(), 
				(vgr.VIRTUAL_HEIGHT - (vgr.VIRTUAL_HEIGHT - buildingRegionStart.getRegionHeight())));
		endCorner.setLocation(buildingRegionEndX, 
				(vgr.VIRTUAL_HEIGHT - (vgr.VIRTUAL_HEIGHT - buildingRegionEnd.getRegionHeight())));

		//Loading Terminal
		term = new Terminal();
		
		//Loading Agent AI
		aai = new AgentAI();

		hero = new Hero(vgr.atlas, startCorner.x, startCorner.y);


		//agent
		agentRegion = vgr.atlas.findRegion("agent");
		TextureRegion[][] agentTR = agentRegion.split(100, 150);
		agentAnimation = new Animation(5.0f, agentTR[0]);
		agentAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		agentAnimatedSprite = new AnimatedSprite(agentAnimation);
		agentX = 50;
		agentY = vgr.VIRTUAL_HEIGHT - agentAnimatedSprite.getHeight();


		//rope
		ropeRegion = vgr.atlas.findRegion("rope");
		//first get the text via Online
		term.textFromURL = term.ReadTextFromURL();
		//then process Text
		term.processText(term.textFromURL);
		//calculate distance hero needs to cross
		term.calculateHeroTravel(startCorner, endCorner);

		System.out.println("start: " + startCorner.x + " " + startCorner.y + "  end" + endCorner.x + " " + endCorner.y );
		citizens = new Array<Citizen>();
		spawnCitizen();


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
		
		checkWinCondition();
		checkGameOverCondition();
		
		//INPUT LOGIC


		batch.begin();


		batch.draw(ropeRegion, startCorner.x, startCorner.y  - 25, endCorner.x - startCorner.x, 25);
		
		batch.draw(buildingRegionStart, buildingRegionStartX, buildingRegionStartY);
		batch.draw(buildingRegionEnd, buildingRegionEndX, buildingRegionEndY);


		hero.heroAnimatedSprite.setX(hero.heroX);
		hero.heroAnimatedSprite.setY(hero.heroY);
		
		hero.heroAnimatedSprite.draw(batch);

		agentAnimatedSprite.draw(batch);
		
		for(Citizen c : citizens)
		{
			c.moveCitizen();
			c.citizenAnimatedSprite.draw(batch);
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
		vgr.font.draw(batch, "PROGRESS " + term.percentageComplete + "%", 50, 700);
		
		//Percentage close to losing to the Agent AI
		vgr.font.setColor(Color.RED);
		vgr.font.draw(batch, "ALERT " + aai.percentage + "%", 50, 740);
		
		//timer
		vgr.font.draw(batch, "TIMER: " + String.valueOf(term.timeLimit - term.timer), 50, 660);
		
		//reset color
		vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));;
		
		vgr.font.setScale(1.0f);
		
//		vgr.font.setScale(0.6f);
//		for(int i = term.currentLinePointer; i <= term.textLinesSize; i++)
//		{
//			vgr.font.drawWrapped(batch, term.textLines[i], 10, 650 - (i * 10), 300);
//		}
//		vgr.font.setScale(1.0f);
		batch.end();


	}

	private void checkGameOverCondition() 
	{
		if(aai.percentage >= 100)
		{
			vgr.setScreen(vgr.gameOverScreen);
			this.hide();
		}
		
		else if(term.timeLimit < 0)
		{
			vgr.setScreen(vgr.gameOverScreen);
			this.hide();
		}
		
	}
	
	private void checkWinCondition()
	{
		if(term.percentageComplete >= 100)
		{
			vgr.setScreen(vgr.transitionScreen);
			this.hide();
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
		gameTheme.play();
		term.startTimer = System.nanoTime();

	}

	@Override
	public void hide() 
	{
		gameTheme.stop();

	}

	@Override
	public void pause() 
	{
		

	}

	@Override
	public void resume() 
	{
		gameTheme.play();

	}

	@Override
	public void dispose() 
	{
		gameTheme.dispose();
		
		//disposing sound
		goodType.dispose();
		badType.dispose();
		

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
		System.out.println("Char: " + character + " Keycode: " + KeyEvent.getExtendedKeyCodeForChar(character));

		//TODO a check here to avoid shift key, control key
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(character);

		//If it's a valid keycode
		if(character != 0)
		{
			term.currentTypedCharacter = character;

			//If player typed the correct letter
			if(term.currentTypedCharacter == term.currentCharacter)
			{
				goodType.play(0.3f);
				moveHero();
				term.completedLine += character;
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
		term.reset(startCorner, endCorner);
		hero.reset(startCorner);
		
	}
	
	public void nextLevel()
	{
		aai.reset();
		term.nextLevel(startCorner, endCorner);
		hero.reset(startCorner);
	}

}
