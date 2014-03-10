package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Cars that move along the mid of buildings. Artistic point for metropolis.
 * @author Biru
 *
 */
public class Car 
{

	Random ran = new Random();
	TextureRegion carRegion;
	Animation carAnimation;
	AnimatedSprite carAnimatedSprite;
	float carX = 0;
	float carY = 0;
	float dx = 1;
	float speed = 0;

	public Car(TextureAtlas atlas, float screenWidth, float screenHeight)
	{
	
		TextureRegion[][] carTR;
		int carSwitch = ran.nextInt(2);
		switch(carSwitch) {
		case 0: carRegion = atlas.findRegion("flyingCar01_ALPHA");
				carTR = carRegion.split(300, 100);
				carAnimation = new Animation(0.1f, carTR[0]);
			    break;
		case 1: carRegion = atlas.findRegion("flyingCar02_ALPHA");
				carTR = carRegion.split(300, 100);
				carAnimation = new Animation(0.1f, carTR[0]);
	    		break;

		}
		
		speed = betweenTwo(2f, 5f);
		carAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		carAnimatedSprite = new AnimatedSprite(carAnimation);
		carAnimatedSprite.setScale(betweenTwo(0.2f, 0.7f));
		carAnimatedSprite.setColor(betweenTwo(0.8f, 1f), betweenTwo(0.9f, 1f), betweenTwo(0.9f, 1f), 1);
		carX = betweenTwo(0, screenWidth);
		carY = betweenTwo(screenHeight/2, screenHeight/2 + 100);;
		
		if(ran.nextInt(2) < 1)
		{
			dx = 1;
		}
		else
		{
			dx = -1;
			carAnimatedSprite.flipFrames(true, false);
		}
	}
	
	public void moveCar()
	{
		
		carX += (dx * speed);
		carAnimatedSprite.setPosition(carX, carY);
	}

	public void checkOffScreen(int virtualWidth, int virtualHeight) 
	{
		if(carX >= virtualWidth + carAnimatedSprite.getWidth() + 200)
		{
			dx *= -1;
			carX -= 100;
			if(dx < 0)
			{
				carAnimatedSprite.flipFrames(true, false);
			}
		}
		
		if(carX <= 0 - carAnimatedSprite.getWidth() - 200)
		{
			dx *= -1;
			carX += 100;
			if(dx > 0)
			{
				carAnimatedSprite.flipFrames(true, false);
			}
		}
		
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
