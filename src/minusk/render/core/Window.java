package minusk.render.core;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLContext;

public class Window {
	private long pointer;
	public final Input input;
	
	Window(long pointer) {
		this.pointer = pointer;
		input = new Input(pointer);
		
		glfwMakeContextCurrent(pointer);
		GLContext.createFromCurrent();
	}
	
	public void update() {
		glfwSwapBuffers(pointer);
	}
	
	public void setVSyncEnabled(boolean state) {
		glfwSwapInterval(state ? 1 : 0);
	}
	
	// Statics
	
	private static GLFWErrorCallback ecb;
	
	public static Window createWindow(int width, int height, String title, int samples) {
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Could not initialized GLFW.");
		glfwSetErrorCallback(ecb==null ? ecb=Callbacks.errorCallbackPrint() : ecb);
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_SAMPLES, samples);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		long window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			return null;
		
		return new Window(window);
	}
}
