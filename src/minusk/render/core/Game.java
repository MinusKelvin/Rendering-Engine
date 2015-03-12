package minusk.render.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import minusk.render.interfaces.Renderable;
import minusk.render.interfaces.Updateable;

public abstract class Game implements Updateable, Renderable {
	private boolean looping;
	
	protected Window window;
	
	public Game(int width, int height, String title) {
		window = Window.createWindow(width, height, title);
	}
	
	protected void gameloop(double gamespeed) {
		initialize();
		
		looping = true;
		double updateInterval = 1.0 / gamespeed;
		double lastup = glfwGetTime();
		
		while (looping) {
			double time = glfwGetTime();
			
			if ((time-lastup)/updateInterval >= 1) {
				
				double updatetime = glfwGetTime();
				int iters = 0;
				while ((int) ((time-lastup)/updateInterval) > 0) {
					update();
					lastup += updateInterval;
					iters++;
				}
				double currenttime = glfwGetTime();
				if (currenttime - updatetime > iters * updateInterval)
					lastup = currenttime;
				
				render();
				
				window.update();
				window.input.resetFrame();
				double polltime = glfwGetTime();
				glfwPollEvents();
				lastup += glfwGetTime() - polltime;
				
				try {
					currenttime = glfwGetTime();
					if ((int) ((updateInterval - (currenttime - lastup)) * 1000) > 0)
						Thread.sleep((int) ((updateInterval - (currenttime - lastup)) * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		cleanUp();
	}
	
	protected abstract void initialize();
	protected abstract void cleanUp();
	
	public void endLoop() {
		looping = false;
	}
}
