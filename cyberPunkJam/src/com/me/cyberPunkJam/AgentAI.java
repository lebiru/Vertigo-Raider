package com.me.cyberPunkJam;

public class AgentAI 
{
	//How close the player is to losing. If 100%, game is lost
	float percentage = 0;
	float decaySpeed = 0.05f;
	
	public AgentAI()
	{
		
	}
	
	/**
	 * Updates all logic regard increasing or decreasing Agent Percentage
	 */
	public void update() 
	{
		//decay percentage
		if(percentage > 0)
		{
			percentage -= decaySpeed;
		}
		
		//bounce percentage up to 0 if negative
		if(percentage < 0 )
		{
			percentage = 0;
		}
		
		roundPercentage();
		
	}

	/**
	 * called everytime percentage is changed. 
	 */
	private void roundPercentage() 
	{
		percentage = (float)Math.round((percentage) * 1000) / 1000;
	}

	/**
	 * This is fired whenever the player presses the wrong currentCharacter key.
	 * Penalizes by increasing the Agent AI percentage.
	 */
	public void wrongKeyImpulse(float deduction) 
	{
		percentage += deduction;
		roundPercentage();
		
	}

	public void reset() 
	{
		percentage = 0;
		roundPercentage();
		
	}
	
	
}
