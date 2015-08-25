package minusk.render.graphics.draw;

import minusk.render.graphics.Camera;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.graphics.globjects.Shader;
import minusk.render.interfaces.Disposable;
import minusk.render.util.Util;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Abstract base class for all drawpasses.
 * 
 * @author MinusKelvin
 */
public abstract class DrawPass implements Disposable {
	public static final int DEFAULT_MAX_POLYGONS = 16384;
	
	private BlendFunc blend = BlendFunc.TRANSPARENCY;
	private final int buffer, maxPolys, bytesPerVertex;
	private boolean hasBegun = false, disposed = false;
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

		glDrawArrays(GL_TRIANGLES, 0, polys*3);
		
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
	public void dispose() {
		if (!disposed) {
			glDeleteBuffers(buffer);
			disposed = true;
		}
	}
	
	@Override
	public boolean isDisposed() {
		return disposed;
	}
	
	public static void initialize() {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glBindVertexArray(glGenVertexArrays());
	}
}
