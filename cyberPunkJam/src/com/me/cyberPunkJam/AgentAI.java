package com.me.cyberPunkJam;

public class AgentAI 
{
	//How close the player is to losing. If 100%, game is lost
	float agentAIpercentage = 0;
	float decaySpeed = 0.05f;
	float tooSlowDeduction = 5;
	int tooSlowThreshold = 4;
	
	public AgentAI()
	{
		
	}
	
	/**
	 * Updates all logic regard increasing or decreasing Agent Percentage
	 */
	public void update() 
	{
		//decay percentage
		if(agentAIpercentage > 0)
		{
			agentAIpercentage -= decaySpeed;
		}
		
		//bounce percentage up to 0 if negative
		if(agentAIpercentage < 0 )
		{
			agentAIpercentage = 0;
		}
		
		roundPercentage();
		
	}

	/**
	 * called everytime percentage is changed. 
	 */
	private void roundPercentage() 
	{
		agentAIpercentage = (float)Math.round((agentAIpercentage) * 1000) / 1000;
	}

	/**
	 * This is fired whenever the player presses the wrong currentCharacter key.
	 * Penalizes by increasing the Agent AI percentage.
	 */
	public void wrongKeyImpulse(float deduction) 
	{
		agentAIpercentage += deduction;
		roundPercentage();	
	}
	
	/**
	 * TooSlowImpulse: Fired when the rateOfTyping is below a certain threshold. 
	 */
	public void tooSlowImpulse(float deduction, boolean isTooSlow)
	{
		if(isTooSlow)
		{
			agentAIpercentage += deduction;
			roundPercentage();
		}
		
	}

	public void reset() 
	{
		agentAIpercentage = 0;
		roundPercentage();
	}
	
	
}
