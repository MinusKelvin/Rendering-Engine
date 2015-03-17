package minusk.render.test;

import minusk.render.core.Game;
import minusk.render.graphics.Color;
import minusk.render.graphics.OrthoCamera;
import minusk.render.graphics.Texture2DArray;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.draw.TexturedDrawPass;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.math.Easing;
import minusk.render.math.Vec2;

public class Test extends Game {
	private ColorDrawPass test;
	private Vec2[] points = {new Vec2(0.1f-1,0), new Vec2(0,-1), null, new Vec2(0,1), new Vec2(1-0.1f,0)};
	
	public Test() {
		super(576, 576, "Game", 8);
	}

	public static void main(String[] args) {
		new Test().gameloop(60, 10);
	}

	@Override
	public void update() {
		if (window.input.closeRequested())
			endLoop();
		
		points[2] = new Vec2(window.input.getMouseX() / 288f - 1, -window.input.getMouseY() / 288f + 1);
	}

	@Override
	public void render() {
		long t = System.nanoTime();
		test.begin();
		
		for (float f = 0; f < 1; f+=0.001) {
			Vec2 p = Easing.bezeir(points, f);
			test.drawRectangle(p.x-0.01f, p.y-0.01f, p.x+0.01f, p.y+0.01f, Color.Red);
			for (int i = 0; i < points.length-1; i++) {
				p = Easing.linear(points[i], points[i+1], f);
				test.drawRectangle(p.x-0.01f, p.y-0.01f, p.x+0.01f, p.y+0.01f, Color.Orange);
			}
		}
		
		test.end();
		System.out.println((System.nanoTime()-t)/1000000);
	}

	@Override
	protected void initialize() {
//		Texture2DArray texture = new Texture2DArray(16, 16, 1, 1, true);
//		texture.setTextureData(getClass().getResourceAsStream("/minusk/render/test/test.png"), 0, 0);
		test = new ColorDrawPass();
		test.setBlendFunc(BlendFunc.OVERWRITE);
	}

	@Override
	protected void cleanUp() {
		
	}
}
