package minusk.render.test;

import minusk.render.core.Game;
import minusk.render.graphics.Color;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.filters.BlendFunc;

public class Test extends Game {
	private ColorDrawPass test;
	
	public Test() {
		super(1024, 576, "Game", 8);
	}

	public static void main(String[] args) {
		new Test().gameloop(60, 10);
	}

	@Override
	public void update() {
		if (window.input.closeRequested())
			endLoop();
	}

	@Override
	public void render() {
		test.begin();
		test.end();
	}

	@Override
	protected void initialize() {
		test = new ColorDrawPass();
		test.setBlendFunc(BlendFunc.OVERWRITE);
	}

	@Override
	protected void cleanUp() {
		
	}
}
