package com.me.cyberPunkJam;

import java.awt.event.KeyEvent;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen, InputProcessor
{
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private VertigoRaiderGame vgr;

	private Sound badType;
	private Sound goodType;

	FileHandle handle = Gdx.files.classpath("data/melissa.txt");

	//Terminal Logic Variables///////////////////////

	//For all lines
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
	int numOfCharactersTypedCorrectly = 0;
	int numOfCharactersTypedTotal = 0;
	int numOfCharactersTypedIncorrectly = 0; 

	////////////////////////////////////////////////////
	Random ran = new Random();

	public GameScreen(final VertigoRaiderGame vgr) 
	{
		this.vgr = vgr;
		this.batch = vgr.batch;
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, vgr.w, vgr.h);
		Gdx.input.setInputProcessor(this);

		vgr.font.setColor(Color.GREEN);
		vgr.font.setScale(1.5f);

		goodType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/type.wav"));
		badType = Gdx.audio.newSound(Gdx.files.internal("Sound/FX/hit.wav"));

		if(handle.exists())
		{
			System.out.println("File exists");
			processText(handle.readString());
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
		int charactersPerLine = 80;
		//All the lines for the text in a string array
		textLines = splitStringEvery(textFileString, charactersPerLine);
		System.out.println(textLines.length);
		//The number of lines from the text
		textLinesSize = textLines.length - 1;
		//Remove Ctrl+Enter, Enter characters
		cleanUpTextLines(textLines);
		
		//The current line we are at, used as a pointer
		currentLinePointer = 0;

		currentLine = textLines[currentLinePointer];
		currentLineArray = currentLine.toCharArray();
		sizeOfCurrentLineArray = currentLineArray.length;
		currentCharacter = currentLineArray[0];

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
//			//for each character in a text line
//			for(int currentChar = 0; currentChar <= textLines2[currentLine].length(); currentChar++)
//			{
//				textLines2[currentLine].
//			}
			textLines2[currentLine] = textLines2[currentLine].replace("\n", " "); 
			textLines2[currentLine] = textLines2[currentLine].replace("\r", ""); 
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
		Gdx.gl.glClearColor(0, 0, 0.2f, 1); // 1 Alpha = no transparency
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		//INPUT LOGIC


		batch.begin();


		vgr.font.setColor(Color.rgba8888(0.2f, ran.nextFloat(), 0.2f, 1));
		vgr.font.draw(batch, String.valueOf(currentCharacter), vgr.w/3, 800);
		vgr.font.draw(batch, currentLine, vgr.w/4, 900);
		vgr.font.draw(batch, "Total: " + numOfCharactersTypedTotal + " \n Correct: " 
				+ numOfCharactersTypedCorrectly + " \n Incorret: " 
				+ numOfCharactersTypedIncorrectly, vgr.w/4, 700);

		for(int i = 0; i <= textLinesSize; i++)
		{
			vgr.font.draw(batch, "Line " + i + ": " + textLines[i], vgr.w/3, 600 - (i * 20));
		}




		batch.end();


	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
