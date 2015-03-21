package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.Arrays;

import minusk.render.graphics.Color;
import minusk.render.interfaces.Renderable;
import minusk.render.interfaces.Updateable;

public class ParticleSystem extends DrawPass implements Updateable,Renderable {
	private Particle[] particles = new Particle[64];
	private int size = 0;
	
	public ParticleSystem() {
		super(DEFAULT_MAX_POLYGONS, 16);
		shader = ColorDrawPass.getColorShader();
		cameraLocation = ColorDrawPass.getProjloc();
	}
	
	@Override
	public void update() {
		for (int i = 0; i < size; i++) {
			if (particles[i].update()) {
				if (i == size-1)
					particles[i] = null;
				else {
					particles[i--] = particles[size-1];
					particles[size-1] = null;
				}
				size--;
			}
		}
	}
	
	@Override
	public void render() {
		begin();
		
		for (int i = 0; i < size; i++)
			particles[i].render();
		
		end();
	}
	
	public void generateParticle(float x, float y, float xvel, float yvel, float rot, float size, Color color) {
		generateParticle(x,y,xvel,yvel,rot,size,color,ParticleSystem::defaultParticleUpdate);
	}
	
	public void generateParticle(float x, float y, float xvel, float yvel, float rot, float size, Color color, ParticleUpdate update) {
		if (this.size == particles.length)
			particles = Arrays.copyOf(particles, particles.length*2);
		Particle p  = new Particle();
		p.update = update;
		p.x = x;
		p.y = y;
		p.xvel = xvel;
		p.yvel = yvel;
		p.rotvel = rot;
		p.color = color;
		p.size = size;
		particles[this.size++] = p;
	}

	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, 16, 12);
		glEnableVertexAttribArray(1);
	}

	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public final class Particle implements Renderable {
		private ParticleUpdate update;
		public float size, x, y, z, angle, rotvel, acceleration, xvel, yvel, zvel, gravityx, gravityy, gravityz;
		public Color color = Color.White;
		public int timeExisted = 0;
		@Override
		public void render() {
			checkDraw(1);
			
			int cv = color.intValue();
			mapped.putFloat((float) ((x-size/2)*Math.cos(angle) + (y-size/2)*-Math.sin(angle)));
			mapped.putFloat((float) ((x-size/2)*Math.sin(angle) + (y-size/2)*Math.cos(angle)));
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			mapped.putFloat((float) (x*Math.cos(angle) + (y+size/2)*-Math.sin(angle)));
			mapped.putFloat((float) (x*Math.sin(angle) + (y+size/2)*Math.cos(angle)));
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			mapped.putFloat((float) ((x+size/2)*Math.cos(angle) + (y-size/2)*-Math.sin(angle)));
			mapped.putFloat((float) ((x+size/2)*Math.sin(angle) + (y-size/2)*Math.cos(angle)));
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			polys++;
		}
		public boolean update() {
			timeExisted++;
			return update.invoke(this);
		}
	}
	
	public static interface ParticleUpdate {
		public boolean invoke(Particle p);
	}
	
	public static boolean defaultParticleUpdate(Particle p) {
		p.xvel += p.gravityx;
		p.yvel += p.gravityy;
		p.zvel += p.gravityz;
		p.xvel *= p.acceleration;
		p.yvel *= p.acceleration;
		p.zvel *= p.acceleration;
		p.x += p.xvel;
		p.y += p.yvel;
		p.z += p.zvel;
		p.angle += p.rotvel;
		return p.timeExisted == 30;
	}
}
