package minusk.render.graphics.draw;

import minusk.render.graphics.Color;
import minusk.render.interfaces.Renderable;
import minusk.render.interfaces.Updateable;
import minusk.render.util.LinearGradient;

import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.*;

public class ParticleSystem extends DrawPass implements Updateable,Renderable {
	private ArrayList<Particle> particles = new ArrayList<>();
	private Options options;
	private final float[] trianglePoints = {
			(float) Math.cos(2 * Math.PI * 0f/3), (float) Math.sin(2 * Math.PI * 0f/3),
			(float) Math.cos(2 * Math.PI * 1f/3), (float) Math.sin(2 * Math.PI * 1f/3),
			(float) Math.cos(2 * Math.PI * 2f/3), (float) Math.sin(2 * Math.PI * 2f/3),
	};
	
	public ParticleSystem(Options options) {
		super(DEFAULT_MAX_POLYGONS, 16);
		shader = ColorDrawPass.getColorShader();
		cameraLocation = ColorDrawPass.getProjloc();
		this.options = options;
	}
	
	public ParticleSystem() {
		this(new Options());
	}
	
	@Override
	public void update() {
		for (Iterator<Particle> iterator = particles.iterator(); iterator.hasNext();) {
			Particle p = iterator.next();
			if (p.update()) {
				iterator.remove();
			}
		}
	}
	
	@Override
	public void render() {
		begin();
		
		particles.forEach(Particle::render);
		
		end();
	}
	
	public Options getOptions() {
		return options;
	}
	
	public void setOptions(Options options) {
		if (options == null)
			throw new IllegalArgumentException("options == null!");
		this.options = options;
	}
	
	public void generateParticle(float x, float y, float xvel, float yvel,
	                             float angle, float rotvel, float size, float lifespanFactor, Color color) {
		generateParticle(x, y, 0, xvel, yvel, 0, angle, rotvel, size, lifespanFactor, color);
	}
	
	public void generateParticle(float x, float y, float z,
	                             float xvel, float yvel, float zvel,
	                             float angle, float rotvel,
	                             float size, float lifespanFactor, Color color) {
		if (color == null && options.colorChange == null)
			throw new IllegalArgumentException("color == null and this.options.colorChange == null");
		Particle p  = new Particle();
		p.x = x;
		p.y = y;
		p.z = z;
		p.xvel = xvel;
		p.yvel = yvel;
		p.zvel = zvel;
		p.angle = angle;
		p.rotvel = rotvel;
		if (options.colorChange != null)
			p.color = options.colorChange.get(0);
		else
			p.color = color;
		p.size = size;
		p.lifespanFactor = lifespanFactor;
		particles.add(0, p);
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
		public float size, x, y, z, angle, rotvel, xvel, yvel, zvel, lifespanFactor;
		public Color color = Color.White;
		public float timeExisted = 0;
		@Override
		public void render() {
			checkDraw(1);
			
			if (options.colorChange != null)
				color = options.colorChange.get(timeExisted);
			
			assert(color != null);
			
			int cv = color.intValue();
			float sin = (float) Math.sin(angle);
			float cos = (float) Math.cos(angle);
			mapped.putFloat((trianglePoints[0] * cos + trianglePoints[1] * -sin) * size + x);
			mapped.putFloat((trianglePoints[0]*sin + trianglePoints[1]*cos) * size + y);
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			mapped.putFloat((trianglePoints[2]*cos + trianglePoints[3]*-sin) * size + x);
			mapped.putFloat((trianglePoints[2]*sin + trianglePoints[3]*cos) * size + y);
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			mapped.putFloat((trianglePoints[4]*cos + trianglePoints[5]*-sin) * size + x);
			mapped.putFloat((trianglePoints[4]*sin + trianglePoints[5]*cos) * size + y);
			mapped.putFloat(z);
			mapped.putInt(cv);
			
			polys++;
		}
		public boolean update() {
			timeExisted += options.timestep * lifespanFactor;
			
			xvel += options.gravityX;
			yvel += options.gravityY;
			zvel += options.gravityZ;
			
			xvel *= options.acceleration;
			yvel *= options.acceleration;
			zvel *= options.acceleration;
			
			rotvel *= options.rotationAcceleration;
			
			x += xvel;
			y += yvel;
			z += zvel;
			angle += rotvel;
			
			size += options.sizeIncrement;
			
			return timeExisted >= 1;
		}
	}
	
	public static final class Options {
		public float gravityX, gravityY, gravityZ, acceleration=1, rotationAcceleration=1, sizeIncrement, timestep = 1/64f;
		public LinearGradient colorChange;
	}
}
