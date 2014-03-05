package com.me.cyberPunkJam;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Citizen 
{

	TextureRegion citizenRegion;
	Animation citizenAnimation;
	AnimatedSprite citizenAnimatedSprite;
	float citizenX = 0;
	float citizenY = 0;
	float dx = 1;
	float speed = 5;

	public Citizen(TextureAtlas atlas)
	{
		//citizen
		citizenRegion = atlas.findRegion("citizen");
		TextureRegion[][] citizenTR = citizenRegion.split(100, 150);
		citizenAnimation = new Animation(0.5f, citizenTR[0]);
		citizenAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		citizenAnimatedSprite = new AnimatedSprite(citizenAnimation);
		
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

}
