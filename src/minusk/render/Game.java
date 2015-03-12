package minusk.render;

import minusk.render.interfaces.Renderable;
import minusk.render.interfaces.Updateable;

public abstract class Game implements Updateable, Renderable {
	private boolean looping;
	
	protected void gameloop(double updaterate, double framecap, Updateable updatefunc, Renderable renderfunc) {
		looping = true;
		double updateInterval = 1.0 / updaterate;
		double frameInterval = 1.0 / framecap;
		double ms = System.nanoTime() / 1000000.0;
		while (looping) {
			
		}
	}
	
	public void endLoop() {
		looping = false;
	}
}
