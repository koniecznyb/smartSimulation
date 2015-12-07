package org.konieczny.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.konieczny.bartlomiej.libgdx.GdxGameClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800;
		config.width = 600;
		config.resizable = false;
		config.title = "Smart agent simulation";
		new LwjglApplication(new GdxGameClass(), config);
	}
}
