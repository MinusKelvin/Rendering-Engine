package minusk.render.test;

import minusk.render.core.Game;
import minusk.render.graphics.OrthoCamera;
import minusk.render.graphics.draw.TextDrawPass;

public class Test extends Game {
	private TextDrawPass text;
	private float scale = 1;
	
	public Test() {
		super(1280, 720, "Game", 8);
	}

	public static void main(String[] args) {
		new Test().gameloop(60, 10);
	}

	@Override
	public void update() {
		if (window.input.closeRequested())
			endLoop();
		
		scale *= Math.pow(1.1, window.input.getScrollY());
	}

	@Override
	public void render() {
		text.begin();
		text.drawString(100,300,"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
				"incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation" +
				"ullamcolaboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
				"voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non" +
				"proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 0xffffccff, scale, 700, false);
		text.end();
	}

	@Override
	protected void initialize() {
		text = new TextDrawPass();
		text.camera = new OrthoCamera(0,1280,720,0);
	}

	@Override
	protected void cleanUp() {
		
	}
}
