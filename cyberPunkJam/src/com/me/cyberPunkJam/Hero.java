package com.me.cyberPunkJam;

import java.awt.Point;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Hero 
{
	TextureRegion heroRegion;
	Animation heroAnimation;
	AnimatedSprite heroAnimatedSprite;
	float heroX = 0;
	float heroY = 0;

	public Hero(TextureAtlas atlas, float startX, float startY)
	{
		//hero
		heroRegion = atlas.findRegion("hero_ALPHA");
		TextureRegion[][] heroTR = heroRegion.split(100, 150);
		heroAnimation = new Animation(0.1f, heroTR[0]);
		heroAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		heroAnimatedSprite = new AnimatedSprite(heroAnimation);
		heroX = startX;
		heroY = startY;
		
	}

	public void reset(Point startCorner) 
	{
		heroX = startCorner.x;
		heroY = startCorner.y;
		
	}

}
