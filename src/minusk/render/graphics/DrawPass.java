package minusk.render.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL30.GL_MAP_WRITE_BIT;
import static org.lwjgl.opengl.GL30.glMapBufferRange;

import java.nio.ByteBuffer;

import minusk.render.graphics.filters.BlendFunc;

/**
 * Abstract base class for all drawpasses.
 * 
 * @author MinusKelvin
 */
public abstract class DrawPass {
	public static final int DEFAULT_MAX_VERTICIES = 32768;
	
	private BlendFunc blend = BlendFunc.TRANSPARENCY;
	private final int buffer = glGenBuffers(), buflen;
	private boolean hasBegun = false;
	protected ByteBuffer mapped;
	protected Shader shader;
	
	public DrawPass(int maxVerticies, int floatsPerVertex, int drawmode) {
		buflen = maxVerticies*floatsPerVertex*4;
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, buflen, drawmode);
	}
	
	public DrawPass(int floatsPerVertex) {
		this(DEFAULT_MAX_VERTICIES, floatsPerVertex, GL_STREAM_DRAW);
	}
	
	public DrawPass(int maxVerticies, int floatsPerVertex) {
		this(maxVerticies, floatsPerVertex, GL_STREAM_DRAW);
	}
	
	public void end() {
		hasBegun = false;
		
		glBlendFunc(blend.src.glenum, blend.dst.glenum);
		glUseProgram(shader.id);
		
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glUnmapBuffer(GL_ARRAY_BUFFER);
		
		render();
	}
	
	public void begin() {
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		mapped = glMapBufferRange(GL_ARRAY_BUFFER, 0, buflen, GL_MAP_WRITE_BIT, mapped);
		hasBegun = true;
	}
	
	protected void checkDraw() {
		if (!hasBegun)
			throw new IllegalStateException("Tried to call a draw method outside of a begin() / end().");
	}
	
	protected abstract void render();
	
	public static void initialize() {
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
	}
}
