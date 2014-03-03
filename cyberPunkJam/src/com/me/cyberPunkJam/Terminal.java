package com.me.cyberPunkJam;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Terminal 
{
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

	float pixelsHeroNeedsToTravel = 200; //this should be the distance between building corners
	float pixelsPerMove = 0; //every time user types a correct word, player must move this amount of pixels.

	////////////////////////////////////////////////////

	public Terminal()
	{

	}

	public void calculateHeroTravel(Point start, Point end)
	{
		pixelsHeroNeedsToTravel = (float) start.distance(end);
		pixelsPerMove = pixelsHeroNeedsToTravel / numOfCharactersInText;
	}

	public void processText(String textFileString) 
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

	//TODO create a method for when an end of current Line is reached

	/**
	 * Called whenever a new text needs to be processed for all "Terminal" variables. You should
	 * be calling this whenever a new text file needs to be processed. 
	 * @param textFileString
	 */
	public void processNextLine() 
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
}