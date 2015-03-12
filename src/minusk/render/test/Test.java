package minusk.render.test;

import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL30.glClearBuffer;

import minusk.render.core.Game;
import minusk.render.core.Util;

public class Test extends Game {
	public Test() {
		super(1024, 576, "Game");
	}

	public static void main(String[] args) {
		new Test().gameloop(60);
	}

	@Override
	public void update() {
		if (window.input.closeRequested())
			endLoop();
	}

	@Override
	public void render() {
		glClearBuffer(GL_COLOR, 0, Util.toBuffer(new float[] {0,0,0,0}));
	}

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void cleanUp() {
		
	}
}
