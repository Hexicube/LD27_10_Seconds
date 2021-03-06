package org.tilegames.hexicube.ludumdare27;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter {
	public static LwjglApplicationConfiguration config;
	
	public static void main(String[] args) {
		config = new LwjglApplicationConfiguration();
		config.title = "10 Seconds of life - V5";
		config.width = 800;
		config.height = 600;
		config.useGL20 = false;
		config.vSyncEnabled = true;
		config.useCPUSynch = false;
		config.depth = 1;
		
		new LwjglApplication(new Game(), config);
	}
}