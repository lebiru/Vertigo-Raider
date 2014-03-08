package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Citizen 
{

	Random ran = new Random();
	TextureRegion citizenRegion;
	Animation citizenAnimation;
	AnimatedSprite citizenAnimatedSprite;
	float citizenX = 0;
	float citizenY = 0;
	float dx = 1;
	float speed = 0;

	public Citizen(TextureAtlas atlas, float screenWidth)
	{
		//citizen
		int citizenSwitch = ran.nextInt(5);
		switch(citizenSwitch) {
		case 0: citizenRegion = atlas.findRegion("citizen01_ALPHA");
			    break;
		case 1: citizenRegion = atlas.findRegion("citizen02_ALPHA");
	    		break;
		case 2: citizenRegion = atlas.findRegion("citizen03_ALPHA");
	    		break;
		case 3: citizenRegion = atlas.findRegion("citizen04_ALPHA");
	    		break;
		case 4: citizenRegion = atlas.findRegion("agent_ALPHA");
		}
		TextureRegion[][] citizenTR = citizenRegion.split(100, 150);
		speed = betweenTwo(2f, 5f);
		citizenAnimation = new Animation(0.15f/speed, citizenTR[0]);
		citizenAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		citizenAnimatedSprite = new AnimatedSprite(citizenAnimation);
		citizenAnimatedSprite.setScale(betweenTwo(1f, 1.5f));
		citizenAnimatedSprite.setColor(betweenTwo(0.8f, 1f), betweenTwo(0.9f, 1f), betweenTwo(0.9f, 1f), 1);
		citizenX = betweenTwo(0, screenWidth);
		citizenY = 0;
		
		if(ran.nextInt(2) < 1)
		{
			dx = 1;
		}
		else
		{
			dx = -1;
			citizenAnimatedSprite.flipFrames(true, false);
		}
		
		
	}
	
	public void moveCitizen()
	{
		
		citizenX += (dx * speed);
		citizenAnimatedSprite.setPosition(citizenX, citizenY);
	}

	public void checkOffScreen(int virtualWidth, int virtualHeight) 
	{
		if(citizenX >= virtualWidth + citizenAnimatedSprite.getWidth() + 200)
		{
			dx *= -1;
			citizenX -= 100;
			if(dx < 0)
			{
				citizenAnimatedSprite.flipFrames(true, false);
			}
			
		}
		
		if(citizenX <= 0 - citizenAnimatedSprite.getWidth() - 200)
		{
			dx *= -1;
			citizenX += 100;
			if(dx > 0)
			{
				citizenAnimatedSprite.flipFrames(true, false);
			}
		}
		
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
