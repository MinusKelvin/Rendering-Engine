package minusk.render.test;

import minusk.render.core.Game;
import minusk.render.graphics.Color;
import minusk.render.graphics.OrthoCamera;
import minusk.render.graphics.draw.ColorDrawPass;
import minusk.render.graphics.draw.ParticleSystem;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.math.Vec2;
import minusk.render.util.LinearGradient;

public class Test extends Game {
	private ColorDrawPass test;
	private Vec2 pos = new Vec2();
	private ParticleSystem particles;
	
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
		
		particles.update();
		
//		for (int x = -6; x <= 6; x++) {
			for (int i = 0; i < 10; i++) {
				float velx = 1;//(float) (0.5 - Math.random());
				float vely = (float) (0.5 - Math.random());
				float tmp = (float) Math.hypot(velx, vely);
				velx /= tmp;
				vely /= tmp;
				particles.generateParticle(/*x / 4f*/-1.7f, 0,
						velx * 0.029f /** (float) Math.random()*/, vely * 0.008f * (float) Math.random(),
						(float) (Math.random() * Math.PI * 2), (float) Math.random() * 0.05f, 0.02f, 1, null);
			}
//		}
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
		particles.getOptions().colorChange = new LinearGradient(Color.Red,
				new Color(Color.Red.getR(), Color.Red.getG(), Color.Red.getB(), 0),
				new LinearGradient.Stop(Color.Yellow,0.15f),
				new LinearGradient.Stop(Color.Green,0.3f),
				new LinearGradient.Stop(Color.Cyan,0.45f),
				new LinearGradient.Stop(Color.Blue,0.6f),
				new LinearGradient.Stop(Color.Magenta,0.75f),
				new LinearGradient.Stop(Color.Red,0.9f));
//		particles.getOptions().gravityY = 0.00015f;
		particles.camera = new OrthoCamera(-1.7777777777777777777777777777778f,1.7777777777777777777777777777778f,1,-1);
		particles.setBlendFunc(BlendFunc.TRANSPARENCY);
		particles.getOptions().sizeIncrement = 0.0002f;
		particles.getOptions().timestep = 1/120f;
	}

	@Override
	protected void cleanUp() {
		
	}
}
