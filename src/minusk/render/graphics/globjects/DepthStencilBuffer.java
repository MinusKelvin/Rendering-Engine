package minusk.render.graphics.globjects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;

public class DepthStencilBuffer {
	private final int id, width, height;
	
	public DepthStencilBuffer(int width, int height) {
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);

		if (width < 1)
			throw new IllegalArgumentException("width cannot be < 1");
		if (height < 1)
			throw new IllegalArgumentException("height cannot be < 1");
		
		this.width = width;
		this.height = height;
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_STENCIL, width, height, 0, GL_BGRA, GL_FLOAT, 0);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		glDeleteTextures(id);
	}
}
