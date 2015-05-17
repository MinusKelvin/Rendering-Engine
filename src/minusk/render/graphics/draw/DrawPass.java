package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glMapBuffer;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GLContext;

import minusk.render.graphics.Camera;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.graphics.globjects.Shader;
import minusk.render.util.Util;

/**
 * Abstract base class for all drawpasses.
 * 
 * @author MinusKelvin
 */
public abstract class DrawPass {
	public static final int DEFAULT_MAX_POLYGONS = 16384;
	
	private BlendFunc blend = BlendFunc.TRANSPARENCY;
	private final int buffer, maxPolys, bytesPerVertex;
	private boolean hasBegun = false;
	protected ByteBuffer mapped;
	protected Shader shader;
	protected int polys, cameraLocation;
	
	public Camera camera;
	
	public DrawPass(int maxPolys, int bytesPerVertex, int drawmode) {
		this.maxPolys = maxPolys;
		this.bytesPerVertex = bytesPerVertex;
		buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, maxPolys*bytesPerVertex*3, drawmode);
	}
	
	public DrawPass(int bytesPerVertex) {
		this(DEFAULT_MAX_POLYGONS, bytesPerVertex, GL_STREAM_DRAW);
	}
	
	public DrawPass(int maxVerticies, int bytesPerVertex) {
		this(maxVerticies, bytesPerVertex, GL_STREAM_DRAW);
	}
	
	public void end() {
		hasBegun = false;
		
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glUnmapBuffer(GL_ARRAY_BUFFER);
		
		glBlendFunc(blend.src.glenum, blend.dst.glenum);
		shader.use();
		
		
		if (camera == null)
			glUniformMatrix4fv(cameraLocation, false, Util.toBuffer(new float[] {1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1}));
		else {
			camera.update();
			glUniformMatrix4fv(cameraLocation, false, Util.toBuffer(camera.combined));
		}

		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		preRender();

		System.out.println(GLContext.translateGLErrorString(glGetError()));
		glDrawArrays(GL_TRIANGLES, 0, polys*3);
		System.out.println(GLContext.translateGLErrorString(glGetError()));
		
		postRender();
	}
	
	public void begin() {
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		mapped = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, maxPolys*bytesPerVertex*3, mapped);
		mapped.position(0);
		polys = 0;
		hasBegun = true;
	}
	
	protected void checkDraw(int polysToDraw) {
		if (!hasBegun)
			throw new IllegalStateException("Tried to call a draw method outside of a begin() / end().");
		if (polys+polysToDraw > maxPolys) {
			end();
			begin();
		}
	}

	public void setBlendFunc(BlendFunc blendFunc) {
		blend = blendFunc;
	}
	
	public void setShader(Shader s, int cameraLocation) {
		shader = s;
		this.cameraLocation = cameraLocation;
	}
	
	protected abstract void preRender();
	protected abstract void postRender();
	
	@Override
	protected void finalize() {
		glDeleteBuffers(buffer);
	}
	
	public static void initialize() {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glBindVertexArray(glGenVertexArrays());
	}
}
