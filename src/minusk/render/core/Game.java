package minusk.render.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL30.glClearBuffer;
import minusk.render.graphics.draw.DrawPass;
import minusk.render.graphics.globjects.Framebuffer;
import minusk.render.interfaces.Renderable;
import minusk.render.interfaces.Updateable;
import minusk.render.util.Util;

public abstract class Game implements Updateable, Renderable {
	private boolean looping;
	
	protected Window window;
	
	public Game(int width, int height, String title, int samples) {
		window = Window.createWindow(width, height, title, samples);
		Framebuffer.setDefaultFramebufferDimensions(width, height);
	}
	
	protected void gameloop(double gamespeed, int maxFrameskip) {
		DrawPass.initialize();
		initialize();
		
		looping = true;
		double updateInterval = 1.0 / gamespeed;
		double lastup = glfwGetTime();
		
		while (looping) {
			double time = glfwGetTime();
			
			if ((time-lastup)/updateInterval >= 1) {
				
				double updatetime = glfwGetTime();
				int iters = 0;
				while ((int) ((time-lastup)/updateInterval) > 0 || iters >= maxFrameskip) {
					update();
					lastup += updateInterval;
					iters++;
				}
				double currenttime = glfwGetTime();
				if (currenttime - updatetime > iters * updateInterval)
					lastup = currenttime;

				glClearBuffer(GL_COLOR, 0, Util.toBuffer(new float[] {0,0,0,0}));
				glClearBuffer(GL_DEPTH, 0, Util.toBuffer(new float[] {1}));
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
