package com.me.cyberPunkJam;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		cfg.title = "Vertigo Raider";
		cfg.useGL20 = false;
		cfg.width = (int) screenDimension.getWidth();
		cfg.height = (int) screenDimension.getHeight();
		cfg.vSyncEnabled = true;
		cfg.fullscreen = false;
		
		new LwjglApplication(new VertigoRaiderGame(), cfg);
	}
}
