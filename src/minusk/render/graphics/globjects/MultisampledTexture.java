package minusk.render.graphics.globjects;

import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetTexLevelParameteri;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_SAMPLES;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

public class MultisampledTexture {
	public final int width, height, samples, id;

	public MultisampledTexture(int width, int height, int samples, boolean hdr) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, id);
		
		if (width < 1)
			throw new IllegalArgumentException("width cannot be < 1");
		if (height < 1)
			throw new IllegalArgumentException("height cannot be < 1");
		if (samples < 1)
			throw new IllegalArgumentException("samples cannot be < 1");
		
		glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples, hdr?GL_RGBA32F:GL_RGBA8, width, height, true);
		this.samples = glGetTexLevelParameteri(GL_TEXTURE_2D_MULTISAMPLE, 0, GL_TEXTURE_SAMPLES);
	}
	
	@Override
	protected void finalize() {
		glDeleteTextures(id);
	}
}
