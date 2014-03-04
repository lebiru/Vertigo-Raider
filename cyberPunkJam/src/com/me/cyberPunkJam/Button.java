package com.me.cyberPunkJam;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button 
{

	TextureRegion texture;
	Rectangle rect;
	float buttonX;
	float buttonY;
	
	float screenWidth;
	float screenHeight;
	

	public Button(AtlasRegion findRegion, int buttonX, int buttonY) 
	{
		this.texture = new TextureRegion(findRegion);
		this.buttonX = buttonX;
		this.buttonY = buttonY;
		
		screenWidth = 1366;
		screenHeight = 786;
		
		this.rect = new Rectangle(this.buttonX, screenHeight - this.buttonY - texture.getRegionHeight(), texture.getRegionWidth(), texture.getRegionHeight());
	}
	
	public boolean isClicked(Vector2 mousePos, boolean mouseClicked)
	{
		
		if(rect.contains(mousePos) && mouseClicked)
		{
			return true;
		}
		
		else 
		{
			return false;
		}
	}

}
