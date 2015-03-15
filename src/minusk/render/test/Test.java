package minusk.render.test;

import minusk.render.core.Game;
import minusk.render.graphics.OrthoCamera;
import minusk.render.graphics.Texture2DArray;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.draw.TexturedDrawPass;
import minusk.render.graphics.filters.BlendFunc;

public class Test extends Game {
	private TexturedDrawPass test;
	
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
		for (int i = 0; i < 16; i++)
			test.drawRectangle(32+i*24, 32+i*24, 0, 1, 0, 64+i*24, 64+i*24, 1, 0, 0);
		test.end();
	}

	@Override
	protected void initialize() {
		Texture2DArray texture = new Texture2DArray(16, 16, 1, 1, true);
		texture.setTextureData(getClass().getResourceAsStream("/minusk/render/test/test.png"), 0, 0);
		test = new TexturedDrawPass(texture);
		test.setBlendFunc(BlendFunc.OVERWRITE);
		test.camera = new OrthoCamera(0, 1024, 576, 0);
	}

	@Override
	protected void cleanUp() {
		
	}
}
