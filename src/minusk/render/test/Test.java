package minusk.render.test;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import java.util.Arrays;

import minusk.render.core.Game;
import minusk.render.graphics.Color;
import minusk.render.graphics.OrthoCamera;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.math.Easing;
import minusk.render.math.Vec2;
import minusk.render.util.Util;

public class Test extends Game {
	private ColorDrawPass test;
	private Vec2[] points = {new Vec2()};
	private float res = 0.125f;
	
	public Test() {
		super(1024, 576, "Game", 8);
	}

	public static void main(String[] args) {
		new Test().gameloop(20, 10);
	}

	@Override
	public void update() {
		if (window.input.closeRequested())
			endLoop();
		
		if (window.input.isMouseTapped(GLFW_MOUSE_BUTTON_LEFT)) {
			points = Arrays.copyOf(points, points.length+1);
			points[points.length-1] = new Vec2(window.input.getMouseX(), window.input.getMouseY());
		}
		if (window.input.isKeyTapped(GLFW_KEY_UP)) {
			res /= 2;
		}
		if (window.input.isKeyTapped(GLFW_KEY_DOWN)) {
			res *= 2;
		}
	}

	@Override
	public void render() {
		long t = System.nanoTime();
		test.begin();
		
		for (int i = 0; i < points.length-1; i++) {
			test.drawLine(points[i].x, points[i].y, points[i+1].x, points[i+1].y, 2, Color.Blue);
		}
		
		Vec2 lp = Easing.bezier(points, 0);
		for (float f = res; ; f+=res) {
			f = Math.min(f, 1);
			Vec2 p = Easing.bezier(points, f);
			test.drawLine(p.x, p.y, lp.x, lp.y, 4, new Color(1,f,0));
			lp = p;
			if (f == 1)
				break;
		}
		
		test.end();
		System.out.println((System.nanoTime()-t)/1000000 + ", " + 1/res);
	}

	@Override
	protected void initialize() {
//		Texture2DArray texture = new Texture2DArray(16, 16, 1, 1, true);
//		texture.setTextureData(getClass().getResourceAsStream("/minusk/render/test/test.png"), 0, 0);
		test = new ColorDrawPass();
		test.setBlendFunc(BlendFunc.OVERWRITE);
		test.camera = new OrthoCamera(0, 1024, 0, 576);
	}

	@Override
	protected void cleanUp() {
		
	}
}
