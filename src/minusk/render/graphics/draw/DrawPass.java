package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glMapBuffer;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;

import minusk.render.graphics.Camera;
import minusk.render.graphics.filters.BlendFunc;
import minusk.render.util.Shader;
import minusk.render.util.Util;

/**
 * Abstract base class for all drawpasses.
 * 
 * @author MinusKelvin
 */
public abstract class DrawPass {
	public static final int DEFAULT_MAX_VERTICIES = 32768;
	
	private BlendFunc blend = BlendFunc.TRANSPARENCY;
	private final int buffer, maxVerticies, bytesPerVertex;
	private boolean hasBegun = false;
	protected ByteBuffer mapped;
	protected Shader shader;
	protected int verticies, cameraLocation;
	
	public Camera camera;
	
	public DrawPass(int maxVerticies, int bytesPerVertex, int drawmode) {
		this.maxVerticies = maxVerticies;
		this.bytesPerVertex = bytesPerVertex;
		buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, maxVerticies*bytesPerVertex, drawmode);
	}
	
	public DrawPass(int floatsPerVertex) {
		this(DEFAULT_MAX_VERTICIES, floatsPerVertex, GL_STREAM_DRAW);
	}
	
	public DrawPass(int maxVerticies, int floatsPerVertex) {
		this(maxVerticies, floatsPerVertex, GL_STREAM_DRAW);
	}
	
	public void end() {
		hasBegun = false;
		
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glUnmapBuffer(GL_ARRAY_BUFFER);
		
		glBlendFunc(blend.src.glenum, blend.dst.glenum);
		shader.use();
		
		
		if (camera == null)
			glUniformMatrix4(cameraLocation, false, Util.toBuffer(new float[] {1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1}));
		else {
			camera.update();
			glUniformMatrix4(cameraLocation, false, Util.toBuffer(camera.combined));
		}

		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		preRender();
		
		glDrawArrays(GL_TRIANGLES, 0, verticies);
		
		postRender();
	}
	
	public void begin() {
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		mapped = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, maxVerticies*bytesPerVertex, mapped);
		verticies = 0;
		hasBegun = true;
	}
	
	protected void checkDraw(int verticiesToDraw) {
		if (!hasBegun)
			throw new IllegalStateException("Tried to call a draw method outside of a begin() / end().");
		if (verticies+verticiesToDraw > maxVerticies) {
			end();
			begin();
		}
	}

	public void setBlendFunc(BlendFunc blendFunc) {
		blend = blendFunc;
	}
	
	protected abstract void preRender();
	protected abstract void postRender();
	
	public static void initialize() {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glBindVertexArray(glGenVertexArrays());
	}
}
