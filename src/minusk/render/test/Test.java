package minusk.render.test;

import static org.lwjgl.opengl.GL11.glGetError;
import minusk.render.core.Game;
import minusk.render.graphics.Color;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.draw.ParticleSystem;
import minusk.render.math.Vec2;

public class Test extends Game {
	private ColorDrawPass test;
	private Vec2 pos = new Vec2();
	private ParticleSystem particles;
	
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

		particles.update();
		particles.generateParticle(0, 0, (float)(Math.random()-0.5)/4, (float)(Math.random()-0.5)/4, 0.1f, 0.1f, Color.White);
	}

	@Override
	public void render() {
		particles.render();
	}

	@Override
	protected void initialize() {
//		Texture2DArray texture = new Texture2DArray(16, 16, 1, 1, true);
//		texture.setTextureData(getClass().getResourceAsStream("/minusk/render/test/test.png"), 0, 0);
		particles = new ParticleSystem();
	}

	@Override
	protected void cleanUp() {
		
	}
}
