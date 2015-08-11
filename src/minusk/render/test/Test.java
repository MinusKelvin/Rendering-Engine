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
	
	private Color[] colors = {
			new Color(2,2,2),
			new Color(2,2,1),
			new Color(2,2,0),
			new Color(2,1,2),
			new Color(2,1,1),
			new Color(2,1,0),
			new Color(2,0,2),
			new Color(2,0,1),
			new Color(2,0,0),
			new Color(1,2,2),
			new Color(1,2,1),
			new Color(1,2,0),
			new Color(1,1,2),
			new Color(1,0,2),
			new Color(0,2,2),
			new Color(0,2,1),
			new Color(0,2,0),
			new Color(0,1,2),
			new Color(0,0,2),
	};
	
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
		
		particles.update();
		
		for (int x = -9; x <= 9; x++) {
			for (int i = 0; i < 7; i++) {
				float velx = /*1;//*/(float) (0.5 - Math.random());
				float vely = (float) (0.5 - Math.random());
				float tmp = (float) Math.hypot(velx, vely);
				velx /= tmp;
				vely /= tmp;
				particles.generateParticle(x / 6f, 0,
						velx * 0.002f * (float) Math.random(), vely * 0.005f * (float) Math.random(),
						(float) (Math.random() * Math.PI * 2), (float) Math.random() * 0.05f, 0.07f, 1,
						colors[x+9]);
			}
		}
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
//		particles.getOptions().colorChange = new LinearGradient(Color.Red,
//				new Color(Color.Yellow.getR(), Color.Yellow.getG(), Color.Yellow.getB(), 0),
//				new LinearGradient.Stop(Color.Orange,0.75f));
		particles.getOptions().gravityY = 0.00015f;
		particles.camera = new OrthoCamera(-1.7777777777777777777777777777778f,1.7777777777777777777777777777778f,1,-1);
		particles.setBlendFunc(BlendFunc.ADDITIVE);
//		particles.getOptions().sizeIncrement = 0.0005f;
		particles.getOptions().timestep = 1/60f;
	}

	@Override
	protected void cleanUp() {
		
	}
}
