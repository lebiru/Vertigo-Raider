package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class is responsible for creating a sign that is located
 * on the sides of the buildings.
 * @author Biru
 *
 */
public class Sign 
{
	Random ran = new Random();
	TextureRegion signRegion;
	Animation signAnimation;
	AnimatedSprite signAnimatedSprite;
	float x = 0;
	float y = 0;

	public Sign(TextureAtlas atlas, float x, float y) 
	{
		signRegion = atlas.findRegion("sign");
		TextureRegion[][] signTR = signRegion.split(200, 100);
		signAnimation = new Animation(betweenTwo(0.5f, 1.0f), signTR[0]);
		signAnimation.setPlayMode(Animation.LOOP);
		signAnimatedSprite = new AnimatedSprite(signAnimation);
		this.x = x;
		this.y = y;
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
