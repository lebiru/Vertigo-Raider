package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Clouds that move along the top-mid of buildings. Artistic point for height.
 * @author Biru
 *
 */
public class Cloud 
{

	Random ran = new Random();
	TextureRegion cloudRegion;
	Animation cloudAnimation;
	AnimatedSprite cloudAnimatedSprite;
	float cloudX = 0;
	float cloudY = 0;
	float dx = 1;
	float speed = 0;

	public Cloud(TextureAtlas atlas, float screenWidth, float screenHeight)
	{
		//citizen
		int citizenSwitch = ran.nextInt(2);
		switch(citizenSwitch) {
		case 0: cloudRegion = atlas.findRegion("cloud01_ALPHA");
			    break;
		case 1: cloudRegion = atlas.findRegion("cloud02_ALPHA");
	    		break;

		}
		TextureRegion[][] citizenTR = cloudRegion.split(307, 122);
		speed = betweenTwo(1f, 4f);
		cloudAnimation = new Animation(1f, citizenTR[0]);
		cloudAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		cloudAnimatedSprite = new AnimatedSprite(cloudAnimation);
		cloudAnimatedSprite.setScale(betweenTwo(1f, 1.5f));
		cloudAnimatedSprite.setColor(betweenTwo(0.8f, 1f), betweenTwo(0.9f, 1f), betweenTwo(0.9f, 1f), 1);
		cloudX = betweenTwo(0, screenWidth);
		cloudY = betweenTwo(screenHeight/2, screenHeight/2 + 100);;
		
		if(ran.nextInt(2) < 1)
		{
			dx = 1;
		}
		else
		{
			dx = -1;
		}
		
		
	}
	
	public void moveCloud()
	{
		
		cloudX += (dx * speed);
		cloudAnimatedSprite.setPosition(cloudX, cloudY);
	}

	public void checkOffScreen(int virtualWidth, int virtualHeight) 
	{
		if(cloudX >= virtualWidth + cloudAnimatedSprite.getWidth() + 200)
		{
			dx *= -1;
			cloudX -= 100;	
		}
		
		if(cloudX <= 0 - cloudAnimatedSprite.getWidth() - 200)
		{
			dx *= -1;
			cloudX += 100;
		}
		
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
