package com.me.cyberPunkJam;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen, InputProcessor
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private VertigoRaiderGame vgr;

	private Sound badType;
	private Sound goodType;

	TextureAtlas atlas;
	TextureRegion buildingRegionStart;
	float buildingRegionStartX = 0;
	float buildingRegionStartY = 0;
	Point startCorner = new Point();
	
	TextureRegion buildingRegionEnd;
	float buildingRegionEndX = 0;
	float buildingRegionEndY = 0;
	Point endCorner = new Point();
	
	TextureRegion heroRegion;
	Animation heroAnimation;
	AnimatedSprite heroAnimatedSprite;
	float heroX = 0;
	float heroY = 0;
	
	TextureRegion ropeRegion;

	float pixelsHeroNeedsToTravel = 200; //this should be the distance between building corners
	float pixelsPerMove = 0; //every time user types a correct word, player must move this amount of pixels.
	
	//Terminal Logic Variables///////////////////////

	//For all lines
	String textFromURL = "";
	String []textLines = {}; //All the lines from a given text
	int textLinesSize = 0;
	int currentLinePointer = 0;

	//For an individual line
	String currentLine = "";
	char[] currentLineArray = {};
	int sizeOfCurrentLineArray = 0;
	int currentCharacterPointer = 0;
	char currentCharacter;
	char currentTypedCharacter;

	float percentageComplete = 0;
	float numOfCharactersInText = 0;
	float numOfCharactersTypedCorrectly = 0;
	float numOfCharactersTypedTotal = 0;
	float numOfCharactersTypedIncorrectly = 0; 

	////////////////////////////////////////////////////
	Random ran = new Random();
	

	public GameScreen(final VertigoRaiderGame vgr) 
	{
		this.vgr = vgr;
		this.batch = vgr.batch;
		this.camera = new OrthographicCamera();
		//camera.setToOrtho(false, vgr.w, vgr.VIRTUAL_HEIGHT);
		Gdx.input.setInputProcessor(this);

		vgr.font.setColor(Color.GREEN);
		vgr.font.setScale(1.0f);

		//LOADING SOUND
		goodType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/type.wav"));
		badType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hit.wav"));

		//LOADING IMAGES
		atlas = new TextureAtlas("Art/Atlas.txt");
		
		//buildings
		buildingRegionStart = atlas.findRegion("building");
		buildingRegionEnd = atlas.findRegion("building");
		
		//setting building locations
		buildingRegionStartX = -100;
		buildingRegionStartY = -400;
		
		buildingRegionEndX = vgr.VIRTUAL_WIDTH - 200;
		buildingRegionEndY = -400;
		
		//hero
		heroRegion = atlas.findRegion("hero");
		TextureRegion[][] heroTR = heroRegion.split(100, 150);
		heroAnimation = new Animation(0.5f, heroTR[0]);
		heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		heroAnimatedSprite = new AnimatedSprite(heroAnimation);
		
		heroX = buildingRegionStart.getRegionX() + buildingRegionStart.getRegionWidth();
		heroY = vgr.VIRTUAL_HEIGHT - (buildingRegionStart.getRegionHeight() - vgr.VIRTUAL_HEIGHT);
		
		//rope
		ropeRegion = atlas.findRegion("rope");
		

		//first get the text via Online
		textFromURL = ReadTextFromURL();
		//then process Text
		processText(textFromURL);


	}

	/**
	 * Credits to Byron Kiourtzoglou
	 * http://examples.javacodegeeks.com/core-java/net/url/read-text-from-url/
	 * @return
	 */
	public String ReadTextFromURL() 
	{

		String holder = "";
		URL url;

		try {

			//Viruses in PlainText via URL

			//Zeus
			//url = new URL("https://gist.githubusercontent.com/zen6/9300973/raw/682f04f8d6a7723887d674e2496cf956e7bffedc/Zeus");

			//Melissa
			//url = new URL("https://gist.githubusercontent.com/zen6/9300956/raw/e512a79ab7a63dd284155c9ed0b79d9e5e3b7183/Melissa");

			//iloveyou
			//url = new URL("https://gist.githubusercontent.com/zen6/9299803/raw/af667792c177130a27fec2b61d3342bea2cbba38/iloveyou");

			//iloveyou
			url = new URL("https://gist.githubusercontent.com/zen6/9301336/raw/9e6aeb022a4080d6b6a9039560addd37e4adf42c/test");

			
			// read text returned by server
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;
			while ((line = in.readLine()) != null) 
			{
				holder += line + "\n"; //adding a newline character for readability
			}
			in.close();
			return holder;

		}
		catch (MalformedURLException e) 
		{
			System.out.println("Malformed URL: " + e.getMessage());
			return "ERROR";
		}
		catch (IOException e) 
		{
			System.out.println("I/O Error: " + e.getMessage());
			return "ERROR";
		}

	}


	//TODO create a method for when an end of current Line is reached

	/**
	 * Called whenever a new text needs to be processed for all "Terminal" variables. You should
	 * be calling this whenever a new text file needs to be processed. 
	 * @param textFileString
	 */
	private void processText(String textFileString) 
	{
		//int charactersPerLine = 80;
		//All the lines for the text in a string array
		//textLines = splitStringEvery(textFileString, charactersPerLine);
		
		textLines = textFileString.split("\\n");
		System.out.println(textLines.length);
		//The number of lines from the text
		textLinesSize = textLines.length - 1;
		//Remove Ctrl+Enter, Enter characters
		cleanUpTextLines(textLines);
		numOfCharactersInText = numOfCharInText(textLines);
		pixelsPerMove = pixelsHeroNeedsToTravel / numOfCharactersInText; 

		//The current line we are at, used as a pointer
		currentLinePointer = 0;

		currentLine = textLines[currentLinePointer];
		currentLineArray = currentLine.toCharArray();
		sizeOfCurrentLineArray = currentLineArray.length;
		currentCharacter = currentLineArray[0];

	}

	private int numOfCharInText(String[] textLines2) 
	{
		int count = 0; 

		for(int numOfLines = 0; numOfLines < textLines2.length; numOfLines++)
		{
			count += textLines2[numOfLines].length();
		}
		
		return count;
		
	}

	/**
	 * Called to remove any banned Characters in a text document.
	 * Banned Characters: Enter, Ctrl+Enter
	 * @param textLines2
	 */
	private void cleanUpTextLines(String[] textLines2) 
	{
		//for each text line
		for(int currentLine = 0; currentLine < textLines2.length; currentLine++)
		{
			textLines2[currentLine] = textLines2[currentLine].replace("\n", ""); 
			textLines2[currentLine] = textLines2[currentLine].replaceFirst(" ", ""); 
		}

	}

	/**
	 * Credits to Aske B. on StackOverFlow for this solution
	 * @param s
	 * @param interval
	 * @return
	 */
	public String[] splitStringEvery(String s, int interval) {
		int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
		String[] result = new String[arrayLength];

		int j = 0;
		int lastIndex = result.length - 1;
		for (int i = 0; i < lastIndex; i++) {
			result[i] = s.substring(j, j + interval);
			j += interval;
		} //Add the last bit
		result[lastIndex] = s.substring(j);

		return result;
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
		percentageComplete = (float)Math.round((numOfCharactersTypedCorrectly/numOfCharactersInText) * 10000) / 100;

		//INPUT LOGIC


		batch.begin();

		
		batch.draw(buildingRegionStart, buildingRegionStartX, buildingRegionStartY);
		batch.draw(buildingRegionEnd, buildingRegionEndX, buildingRegionEndY);
		
		batch.draw(ropeRegion, 300, 525, 866, 25);
		
		heroAnimatedSprite.setX(heroX);
		heroAnimatedSprite.setY(heroY);
		heroAnimatedSprite.draw(batch);

		vgr.font.setColor(Color.rgba8888(0.9f, ran.nextFloat(), 0.9f, 1));
		vgr.font.draw(batch, String.valueOf(currentCharacter), vgr.VIRTUAL_WIDTH/4, 680);
		vgr.font.draw(batch, currentLine, vgr.VIRTUAL_WIDTH/4, 720);
		vgr.font.draw(batch, "Total: " + numOfCharactersTypedTotal + " \n Correct: " 
				+ numOfCharactersTypedCorrectly + " \n Incorrect: " 
				+ numOfCharactersTypedIncorrectly + " Percentage Complete: " + percentageComplete + "%"
				+ "Distance: " + pixelsHeroNeedsToTravel
				+ " Pixels Per Move" + pixelsPerMove 
				, vgr.VIRTUAL_WIDTH/4, 700);
		
		vgr.font.draw(batch, "MouseX: " + Gdx.input.getX() + " MouseY: " + Gdx.input.getY() + 
				" heroY: " + heroY,
				vgr.VIRTUAL_WIDTH/4, 740);
		
		
		for(int i = 0; i <= textLinesSize; i++)
		{
			vgr.font.draw(batch, "Line " + i + ": " + textLines[i], vgr.VIRTUAL_WIDTH/4, 600 - (i * 20));
		}

		batch.end();


	}

	@Override
	public void resize(int width, int height) 
	{
		vgr.resize(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
	public void dispose() {
		// TODO Auto-generated method stub

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
			this.currentTypedCharacter = character;

			//If player typed the correct letter
			if(currentTypedCharacter == currentCharacter)
			{
				goodType.play();
				moveHero();
				numOfCharactersTypedCorrectly++;
				numOfCharactersTypedTotal++;

				//if there are still some characters to type on the line
				if(currentCharacterPointer + 1 < sizeOfCurrentLineArray)
				{
					currentCharacterPointer++;
					currentCharacter = currentLineArray[currentCharacterPointer];
				}
				//else if we've reached the end of the line
				else
				{
					currentCharacterPointer = 0;
					processNextLine();
					currentCharacter = currentLineArray[currentCharacterPointer];
				}
			}

			//If player typed the incorrect letter
			else
			{
				badType.play();
				numOfCharactersTypedIncorrectly++;
				numOfCharactersTypedTotal++;
			}
		}




		return false;
	}

	private void moveHero() 
	{
		heroX += pixelsPerMove;
	}

	private void processNextLine() 
	{
		//If there are still lines in the text document
		if(currentLinePointer + 1 <= textLinesSize)
		{
			currentLinePointer++;

			System.out.println(currentLinePointer);
			System.out.println(textLines.toString());
			System.out.println(textLines.length);

			currentLine = textLines[currentLinePointer];
			currentLineArray = currentLine.toCharArray();
			sizeOfCurrentLineArray = currentLineArray.length;
		}

		//if we've reached the end of the document, ie Level Complete
		else
		{
			currentLine = "ACCESS GRANTED";
		}

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

}
