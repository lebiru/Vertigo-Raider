package com.me.cyberPunkJam;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class Terminal 
{
	//Terminal Logic Variables///////////////////////

	Random ran = new Random();
	int currentLevel = 0;


	double timer = 0;
	long startTimer = 0;
	double timeLimit = 0;
	double timeLeft = 0;

	//For all lines
	String textFromURL = "";
	String []textLines = {}; //All the lines from a given text
	int textLinesSize = 0;
	int currentLinePointer = 0;

	//For an individual line
	String currentLine = "";
	String completedLine = "";
	char[] currentLineArray = {};
	int sizeOfCurrentLineArray = 0;
	int currentCharacterPointer = 0;
	char currentCharacter;
	char currentTypedCharacter;

	//statistics
	float percentageComplete = 0;
	float numOfCharactersInText = 0;
	float numOfCharactersTypedCorrectly = 0;
	float numOfCharactersTypedTotal = 0;
	float numOfCharactersTypedIncorrectly = 0; 

	//level
	Array<URL> levels = new Array<URL>();

	//Sequencers
	long sequencerCountdown = 0;
	long sequencerCountdownLimit = 0;
	long sequencerTimer;
	long lastCompletedSequence = 0;
	boolean isSequenceMode = false;
	String[] possibleSequences = {"cyber", "hax0r", "snow", "Zero Cool", "Hiro"};
	String sequence = possibleSequences[ran.nextInt(possibleSequences.length)];
	int sizeOfCurrentLineArraySequencer = sequence.length();
	char currentSequencerCharacter = sequence.charAt(0);
	String completedLineSequencer = "";
	int currentCharacterPointerSequencer = 0;
	char[] currentLineArraySequencer = sequence.toCharArray();
	
	//Rate of Typing Logic
	
	//recalculated every second to determine how many letters are typed every second
	int rateOfTyping = 0;
	//listener between every second that counts the ...
	int numOfCorrectCharactersTyped = 0;
	//logs the current second. if different, we know the timer has updated, then we save it.
	int lastRecordedSecond = 0;

	float pixelsHeroNeedsToTravel = 200; //this should be the distance between building corners
	float pixelsPerMove = 0; //every time user types a correct word, player must move this amount of pixels.


	////////////////////////////////////////////////////

	/**
	 * Class constructor
	 * @throws MalformedURLException 
	 */
	public Terminal(int currentLevel)
	{
		this.currentLevel = currentLevel;
		System.out.println("terminal constructor run, LEVEL: " + currentLevel);
		try {
			makeURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Called on the start of a level. Calculates how much hero needs to travel.
	 * @param start
	 * @param end
	 */
	public void calculateHeroTravel(Point start, Point end)
	{
		pixelsHeroNeedsToTravel = (float) start.distance(end);
		pixelsPerMove = pixelsHeroNeedsToTravel / numOfCharactersInText;
	}

	/**
	 * Called on the start of a level.
	 * Splits the text from the URL.
	 * Then it removes unwanted characters
	 * Then it calculates the number of characters in the text
	 * Then it sets up the Terminal data for the level.
	 * @param textFileString
	 */
	public void processText(String textFileString) 
	{

		//number of lines in this text
		textLines = textFileString.split("\\n");
		//The number of lines from the text
		textLinesSize = textLines.length - 1;
		//Remove Ctrl+Enter, Enter characters
		cleanUpTextLines(textLines);
		numOfCharactersInText = numOfCharInText(textLines);

		//The current line we are at, used as a pointer
		currentLinePointer = 0;
		//setting what the current line is
		currentLine = textLines[currentLinePointer];
		//set the current line to a char array
		currentLineArray = currentLine.toCharArray();
		//get the size of the line
		sizeOfCurrentLineArray = currentLineArray.length;
		//setting the current character pointer for the line to be 0
		currentCharacterPointer = 0;
		//setting the first needed char to be the first char from the line
		currentCharacter = currentLineArray[currentCharacterPointer];

	}

	/**
	 * Calculates the number of characters in the text.
	 * Also assigns time limit info in this class.
	 * @param textLines2
	 * @return
	 */
	private int numOfCharInText(String[] textLines2) 
	{
		int count = 0; 

		for(int numOfLines = 0; numOfLines < textLines2.length; numOfLines++)
		{
			count += textLines2[numOfLines].length();
		}

		timeLimit = count;
		sequencerCountdownLimit = (long) betweenTwo(20f, 50f);
		sequencerCountdown = sequencerCountdownLimit;
		lastCompletedSequence = System.nanoTime();

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
			//textLines2[currentLine] = textLines2[currentLine].replaceFirst(" ", ""); 
		}

	}

	/**
	 * Credits to Aske B. on StackOverFlow for this solution
	 * @param s
	 * @param interval
	 * @return
	 */
	public String[] splitStringEvery(String s, int interval) 
	{
		int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
		String[] result = new String[arrayLength];

		int j = 0;
		int lastIndex = result.length - 1;
		for (int i = 0; i < lastIndex; i++) 
		{
			result[i] = s.substring(j, j + interval);
			j += interval;
		} //Add the last bit
		result[lastIndex] = s.substring(j);

		return result;
	}

	//TODO create a method for when an end of current Line is reached

	/**
	 * Called whenever a new text needs to be processed for all "Terminal" variables. You should
	 * be calling this whenever a new text file needs to be processed. 
	 * @param textFileString
	 */
	public void processNextLine() 
	{

		//clear completed line
		completedLine = "";

		//If there are still lines in the text document
		if(currentLinePointer + 1 <= textLinesSize)
		{
			currentLinePointer++;
			currentLine = textLines[currentLinePointer];
			currentLineArray = currentLine.toCharArray();
			sizeOfCurrentLineArray = currentLineArray.length;
		}

		//if we've reached the end of the document, ie Level Complete
		else
		{
			currentLine = "";
		}

	}


	/**
	 * Credits to Byron Kiourtzoglou
	 * http://examples.javacodegeeks.com/core-java/net/url/read-text-from-url/ 
	 * @return
	 */
	public String ReadTextFromURL() 
	{

		String holder = "";
		try {
			// read text returned by server
			BufferedReader in = new BufferedReader(new InputStreamReader(levels.get(currentLevel).openStream()));

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

	/**
	 * Called whenever Game Over is reached. Resets all game data to first level.
	 * @param startCorner
	 * @param endCorner
	 */
	public void reset(Point startCorner, Point endCorner) 
	{
		percentageComplete = 0;
		completedLine = "";
		processText(textFromURL);
		calculateHeroTravel(startCorner, endCorner);
		//generateSequence();
	}

	/**
	 * Sets up the game for the next level.
	 * @param startCorner
	 * @param endCorner
	 */
	public void nextLevel(Point startCorner, Point endCorner) 
	{
		percentageComplete = 0;
		completedLine = "";
		currentLine = "";
		//first get the text via Online
		textFromURL = ReadTextFromURL();
		//then process Text
		processText(textFromURL);
		//calculate distance hero needs to cross
		calculateHeroTravel(startCorner, endCorner);
		//set the new character
		currentCharacter = currentLineArray[currentCharacterPointer];
		//generate a new sequencer
		//generateSequence();
		System.out.println("terminal nextLevel run, LEVEL: " + currentLevel);

	}

	/**
	 * Updates the tick for the timer.
	 */
	public void updateTimer() 
	{
		timer = Math.round((double)(System.nanoTime() - startTimer) / 1000000000);
		timeLeft = timeLimit - timer;

		sequencerTimer = Math.round((double)(System.nanoTime() - lastCompletedSequence) / 1000000000);
		if(sequencerCountdown > 0)
		{
			sequencerCountdown = (long) ((long) sequencerCountdownLimit - sequencerTimer);
			isSequenceMode = false;
		}
		if(sequencerCountdown <= 0)
		{
			isSequenceMode = true;
		}
	}
	
	/**
	 * updateTooSlowImpulse: Updates the logic for the tooSlow method
	 * currentSecond: the current second as determined by the game timer
	 * threshold: the rate of correct keys the player has to type in order
	 * to avoid getting penalized by the agent AI.
	 */
	public boolean updateTooSlowImpulse(int currentSecond, int threshold)
	{
		//if we are still within the same second window
		if(currentSecond == lastRecordedSecond)
		{
			return false;
		}
		
		//if we are in a new second window
		if(currentSecond != lastRecordedSecond)
		{
			lastRecordedSecond = currentSecond;
			rateOfTyping = numOfCorrectCharactersTyped;
			numOfCorrectCharactersTyped = 0;
			if(rateOfTyping < threshold)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		return false;
	}
	
	//Generate a QTE Sequence
	public void generateSequence()
	{
		sequence = possibleSequences[ran.nextInt(possibleSequences.length)];
		sequencerCountdownLimit = (long) betweenTwo(20f, 50f);
		sequencerCountdown = sequencerCountdownLimit;
		sequencerTimer = Math.round((double)(System.nanoTime() - lastCompletedSequence) / 100000000);
		lastCompletedSequence = System.nanoTime();

		isSequenceMode = false;

		sizeOfCurrentLineArraySequencer = sequence.length();
		currentSequencerCharacter = sequence.charAt(0);
		completedLineSequencer = "";
		currentCharacterPointerSequencer = 0;
		currentLineArraySequencer = sequence.toCharArray();
	}

	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

	/**
	 * Grabs the levels via URL
	 * @throws MalformedURLException
	 */
	public void makeURL() throws MalformedURLException
	{
//		levels.add(new URL("https://gist.githubusercontent.com/zen6/9360518/raw/30d74d258442c7c65512eafab474568dd706c430/short"));
//		levels.add(new URL("https://gist.githubusercontent.com/zen6/9360518/raw/30d74d258442c7c65512eafab474568dd706c430/short"));
		
		levels.add(new URL("https://gist.githubusercontent.com/zen6/9299803/raw/aee25f078f43249043771df64ae0dffc833c3f1d/iloveyou"));
		levels.add(new URL("https://gist.githubusercontent.com/zen6/9300956/raw/d16f38add4d392fa15fdd6dddf235457ea62a264/Melissa"));
		levels.add(new URL("https://gist.githubusercontent.com/zen6/9300973/raw/1e36e23efbbc198543a5644c15c0335cfa676cbc/Zeus"));

	}
}











