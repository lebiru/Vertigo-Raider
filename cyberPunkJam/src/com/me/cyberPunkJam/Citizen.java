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
		citizenRegion = atlas.findRegion("citizen");
		TextureRegion[][] citizenTR = citizenRegion.split(100, 150);
		speed = betweenTwo(2f, 5f);
		citizenAnimation = new Animation(1/speed, citizenTR[0]);
		citizenAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		citizenAnimatedSprite = new AnimatedSprite(citizenAnimation);
		citizenAnimatedSprite.setScale(betweenTwo(1f, 1.5f));
		citizenX = betweenTwo(0, screenWidth);
		citizenY = 0;
		
		if(ran.nextInt(2) < 1)
		{
			dx = 1;
		}
		else
		{
			dx = -1;
		}
		
		
	}
	
	public void moveCitizen()
	{
		
		citizenX += (dx * speed);
		citizenAnimatedSprite.setPosition(citizenX, citizenY);
	}

	public void checkOffScreen(int virtualWidth, int virtualHeight) 
	{
		if(citizenX >= virtualWidth + citizenAnimatedSprite.getWidth() + 500)
		{
			dx *= -1;
		}
		
		else if(citizenX <= 0 - citizenAnimatedSprite.getWidth() - 100)
		{
			dx *= -1;
		}
		
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
