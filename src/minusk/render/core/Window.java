package minusk.render.core;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private long pointer;
	public final Input input;
	
	Window(long pointer) {
		this.pointer = pointer;
		input = new Input(pointer);
		
		glfwMakeContextCurrent(pointer);
		GL.createCapabilities();
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
		glfwSetErrorCallback(ecb==null ? ecb=Callbacks.errorCallbackThrow() : ecb);
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		if (LWJGLUtil.getPlatform() == LWJGLUtil.Platform.MACOSX)
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		else
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_SAMPLES, samples);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		long window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			return null;
		
		return new Window(window);
	}
}
