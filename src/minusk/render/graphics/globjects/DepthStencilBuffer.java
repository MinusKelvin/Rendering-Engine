package minusk.render.graphics.globjects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
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
}
