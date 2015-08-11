package minusk.render.core;

import minusk.render.util.Util;
import org.lwjgl.glfw.*;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class Input {
	private boolean[] keydown = new boolean[GLFW_KEY_LAST + 1];
	private boolean[] keytap = new boolean[GLFW_KEY_LAST + 1];
	private boolean[] mousedown = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
	private boolean[] mousetap = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
	private double mousex, mousey, scrollx, scrolly;
	private StringBuilder typedThisFrame = new StringBuilder();
	private boolean cursorIn=false, shouldClose, focused=true, iconified;
	private int fboWidth, fboHeight, windowWidth, windowHeight, windowX, windowY;

	private KeyCallback key;
	private CharCallback character;
	private ScrollCallback scroll;
	private MouseButtonCallback mousebutton;
	private CursorPosCallback cursorpos;
	private CursorEnterCallback cursorenter;
	private FramebufferSizeCallback framebuffersize;
	private WindowSizeCallback windowsize;
	private WindowCloseCallback windowclose;
	private WindowFocusCallback windowfocus;
	private WindowIconifyCallback windowiconify;
	private WindowPosCallback windowpos;
	private WindowRefreshCallback windowrefresh;
	private DropCallback drop;

	private GLFWKeyCallback keyPersist;
	private GLFWCharCallback characterPersist;
	private GLFWScrollCallback scrollPersist;
	private GLFWMouseButtonCallback mousebuttonPersist;
	private GLFWCursorPosCallback cursorposPersist;
	private GLFWCursorEnterCallback cursorenterPersist;
	private GLFWFramebufferSizeCallback framebuffersizePersist;
	private GLFWWindowSizeCallback windowsizePersist;
	private GLFWWindowCloseCallback windowclosePersist;
	private GLFWWindowFocusCallback windowfocusPersist;
	private GLFWWindowIconifyCallback windowiconifyPersist;
	private GLFWWindowPosCallback windowposPersist;
	private GLFWWindowRefreshCallback windowrefreshPersist;
	private GLFWDropCallback dropPersist;

	Input(long window) {
		resetFrame();

		IntBuffer ib1 = Util.directBuffer(4).asIntBuffer();
		IntBuffer ib2 = Util.directBuffer(4).asIntBuffer();
		glfwGetFramebufferSize(window, ib1, ib2);
		fboHeight = ib2.get(0);
		fboWidth = ib1.get(0);

		glfwGetWindowSize(window, ib1, ib2);
		windowHeight = ib2.get(0);
		windowWidth = ib1.get(0);

		glfwGetWindowPos(window, ib1, ib2);
		windowX = ib1.get(0);
		windowY = ib2.get(0);

		keyPersist = GLFWKeyCallback(this::key);
		glfwSetKeyCallback(window, keyPersist);
		characterPersist = GLFWCharCallback(this::character);
		glfwSetCharCallback(window, characterPersist);
		scrollPersist = GLFWScrollCallback(this::scroll);
		glfwSetScrollCallback(window, scrollPersist);
		mousebuttonPersist = GLFWMouseButtonCallback(this::mouseButton);
		glfwSetMouseButtonCallback(window, mousebuttonPersist);
		cursorposPersist = GLFWCursorPosCallback(this::cursorPos);
		glfwSetCursorPosCallback(window, cursorposPersist);
		cursorenterPersist = GLFWCursorEnterCallback(this::cursorEnter);
		glfwSetCursorEnterCallback(window, cursorenterPersist);
		framebuffersizePersist = GLFWFramebufferSizeCallback(this::framebufferSize);
		glfwSetFramebufferSizeCallback(window, framebuffersizePersist);
		windowsizePersist = GLFWWindowSizeCallback(this::windowSize);
		glfwSetWindowSizeCallback(window, windowsizePersist);
		windowclosePersist = GLFWWindowCloseCallback(this::windowClose);
		glfwSetWindowCloseCallback(window, windowclosePersist);
		windowfocusPersist = GLFWWindowFocusCallback(this::windowFocus);
		glfwSetWindowFocusCallback(window, windowfocusPersist);
		windowiconifyPersist = GLFWWindowIconifyCallback(this::windowIconify);
		glfwSetWindowIconifyCallback(window, windowiconifyPersist);
		windowposPersist = GLFWWindowPosCallback(this::windowPos);
		glfwSetWindowPosCallback(window, windowposPersist);
		windowrefreshPersist = GLFWWindowRefreshCallback(this::windowRefresh);
		glfwSetWindowRefreshCallback(window, windowrefreshPersist);
		dropPersist = GLFWDropCallback(this::drop);
		glfwSetDropCallback(window, dropPersist);
	}

	public void resetFrame() {
		for (int i = 0; i < GLFW_KEY_LAST + 1; i++)
			keytap[i] = false;
		for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST + 1; i++)
			mousetap[i] = false;
		typedThisFrame.delete(0, typedThisFrame.length());
		scrollx = 0;
		scrolly = 0;
	}

	// GETTERS

	public boolean isKeyDown(int key) {
		return keydown[key];
	}

	public boolean isKeyTapped(int key) {
		return keytap[key];
	}

	public boolean isMouseDown(int key) {
		return mousedown[key];
	}

	public boolean isMouseTapped(int key) {
		return mousetap[key];
	}

	public boolean closeRequested() {
		return shouldClose;
	}

	public boolean isFocused() {
		return focused;
	}

	public boolean isIconified() {
		return iconified;
	}

	public boolean isCursorInside() {
		return cursorIn;
	}

	public double getScrollX() {
		return scrollx;
	}

	public double getScrollY() {
		return scrolly;
	}

	public double getMouseXRaw() {
		return mousex;
	}

	public double getMouseYRaw() {
		return mousey;
	}

	public int getMouseX() {
		return (int) mousex;
	}

	public int getMouseY() {
		return (int) mousey;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public int getFramebufferWidth() {
		return fboWidth;
	}

	public int getFramebufferHeight() {
		return fboHeight;
	}

	public int getWindowX() {
		return windowX;
	}

	public int getWindowY() {
		return windowY;
	}

	public String getChars() {
		return typedThisFrame.toString();
	}

	// SETTERS

	public void setKeyDown(int key, boolean value) {
		keydown[key] = value;
	}

	public void setKeyTapped(int key, boolean value) {
		keytap[key] = value;
	}

	public void setMouseDown(int key, boolean value) {
		mousedown[key] = value;
	}

	public void setMouseTapped(int key, boolean value) {
		mousetap[key] = value;
	}

	public void setScrollX(double value) {
		scrollx = value;
	}

	public void setScrollY(double value) {
		scrolly = value;
	}

	public void setMouseXRaw(double value) {
		mousex = value;
	}

	public void setMouseYRaw(double value) {
		mousey = value;
	}

	public void setChars(String chars) {
		typedThisFrame.delete(0, typedThisFrame.length());
		typedThisFrame.append(chars);
	}

	public void appendChars(String chars) {
		typedThisFrame.append(chars);
	}

	// GLFW CALLBACKS

	private void key(long window, int key, int scancode, int action, int mods) {
		if (key != GLFW_KEY_UNKNOWN) {
			keydown[key] = action != GLFW_RELEASE;
			keytap[key] |= action == GLFW_PRESS;
		}
		if (this.key != null)
			this.key.invoke(key, scancode, action, mods);
	}

	private void character(long window, int character) {
		typedThisFrame.append((char) character);
		if (this.character != null)
			this.character.invoke(character);
	}

	private void scroll(long window, double x, double y) {
		scrollx += x;
		scrolly += y;
		if (scroll != null)
			scroll.invoke(x,y);
	}

	private void mouseButton(long window, int button, int action, int mods) {
		mousedown[button] = action == GLFW_PRESS;
		mousetap[button] |= action == GLFW_PRESS;
		if (mousebutton != null)
			mousebutton.invoke(button, action, mods);
	}

	private void cursorPos(long window, double x, double y) {
		mousex = x;
		mousey = y;
		if (cursorpos != null)
			cursorpos.invoke(x, y);
	}

	private void cursorEnter(long window, int state) {
		cursorIn = state == GL_TRUE;
		if (this.cursorenter != null)
			this.cursorenter.invoke(state);
	}

	private void framebufferSize(long window, int width, int height) {
		fboWidth = width;
		fboHeight = height;
		if (this.framebuffersize != null)
			this.framebuffersize.invoke(width, height);
	}

	private void windowSize(long window, int width, int height) {
		windowWidth = width;
		windowHeight = height;
		if (this.windowsize != null)
			this.windowsize.invoke(width, height);
	}

	private void windowClose(long window) {
		shouldClose = true;
		if (this.windowclose != null)
			this.windowclose.invoke();
	}

	private void windowFocus(long window, int state) {
		focused = state == GL_TRUE;
		if (this.windowfocus != null)
			this.windowfocus.invoke(state);
	}

	private void windowIconify(long window, int state) {
		iconified = state == GL_TRUE;
		if (this.windowiconify != null)
			this.windowiconify.invoke(state);
	}

	private void windowPos(long window, int x, int y) {
		windowX = x;
		windowY = y;
		if (this.windowpos != null)
			this.windowpos.invoke(x, y);
	}

	private void windowRefresh(long window) {
		if (this.windowrefresh != null)
			this.windowrefresh.invoke();
	}

	private void drop(long window, int count, long paths) {
		if (this.drop != null)
			this.drop.invoke(Callbacks.dropCallbackNamesString(count, paths));
	}

	// EXTRA CALLBACKS

	public interface KeyCallback {
		void invoke(int key, int scancode, int action, int mods);
	}
	public void setKeyCallback(KeyCallback method) {
		key = method;
	}

	public interface CharCallback {
		void invoke(int character);
	}
	public void setCharCallback(CharCallback method) {
		character = method;
	}

	public interface ScrollCallback {
		void invoke(double x, double y);
	}
	public void setScrollCallback(ScrollCallback method) {
		scroll = method;
	}

	public interface MouseButtonCallback {
		void invoke(int button, int action, int mods);
	}
	public void setMouseButtonCallback(MouseButtonCallback method) {
		mousebutton = method;
	}

	public interface CursorPosCallback {
		void invoke(double x, double y);
	}
	public void setCursorPosCallback(CursorPosCallback method) {
		cursorpos = method;
	}

	public interface CursorEnterCallback {
		void invoke(int state);
	}
	public void setCursorEnterCallback(CursorEnterCallback method) {
		cursorenter = method;
	}

	public interface FramebufferSizeCallback {
		void invoke(int width, int height);
	}
	public void setFramebufferSizeCallback(FramebufferSizeCallback method) {
		framebuffersize = method;
	}

	public interface WindowSizeCallback {
		void invoke(int width, int height);
	}
	public void setWindowResizeCallback(WindowSizeCallback method) {
		windowsize = method;
	}

	public interface WindowCloseCallback {
		void invoke();
	}
	public void setWindowCloseCallback(WindowCloseCallback method) {
		windowclose = method;
	}

	public interface WindowFocusCallback {
		void invoke(int state);
	}
	public void setWindowFocusCallback(WindowFocusCallback method) {
		windowfocus = method;
	}

	public interface WindowIconifyCallback {
		void invoke(int state);
	}
	public void setWindowIconifyCallback(WindowIconifyCallback method) {
		windowiconify = method;
	}

	public interface WindowPosCallback {
		void invoke(int x, int y);
	}
	public void setWindowPosCallback(WindowPosCallback method) {
		windowpos = method;
	}

	public interface WindowRefreshCallback {
		void invoke();
	}
	public void setWindowRefreshCallback(WindowRefreshCallback method) {
		windowrefresh = method;
	}

	public interface DropCallback {
		void invoke(String[] paths);
	}
	public void setDropCallback(DropCallback method) {
		drop = method;
	}
}
