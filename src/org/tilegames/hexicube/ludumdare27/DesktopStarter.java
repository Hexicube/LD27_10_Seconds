package org.tilegames.hexicube.ludumdare27;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {
	public static LwjglApplicationConfiguration config;
	
	public static void main(String[] args) {
		config = new LwjglApplicationConfiguration();
		config.title = "10 Seconds";
		config.width = 712;
		config.height = 512;
		config.useGL20 = false;
		config.vSyncEnabled = true;
		
		new LwjglApplication(new Game(), config);
	}
}