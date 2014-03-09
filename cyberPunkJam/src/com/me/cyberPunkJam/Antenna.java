package com.me.cyberPunkJam;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class is responsible for creating an antenna that is located
 * on the top of the buildings.
 * @author Biru
 *
 */
public class Antenna 
{
	Random ran = new Random();
	TextureRegion antennaRegion;
	Animation antennaAnimation;
	AnimatedSprite antennaAnimatedSprite;
	float angle;
	float x = 0;
	float y = 0;

	public Antenna(TextureAtlas atlas, float x, float y) 
	{
		int signSwitch = ran.nextInt(3);
		TextureRegion[][] signTR;
		switch(signSwitch) {
		case 0: antennaRegion = atlas.findRegion("antena01_ALPHA");
			    break;
		case 1: antennaRegion = atlas.findRegion("antena02_ALPHA");
	    		break;
		case 2: antennaRegion = atlas.findRegion("antena03_ALPHA");
	    		break;

		}
		signTR = antennaRegion.split(300, 200);
		antennaAnimation = new Animation(1f, signTR[0]);
		antennaAnimation.setPlayMode(Animation.LOOP);
		antennaAnimatedSprite = new AnimatedSprite(antennaAnimation);
		antennaAnimatedSprite.setColor(ran.nextFloat(), ran.nextFloat(), ran.nextFloat(), 1f);
		this.x = x;
		this.y = y;
	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}
