package minusk.render.graphics.globjects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import minusk.render.util.Util;

public class Texture {
	public final int width, height, mipmapLevels, id;
	
	public Texture(int width, int height, int mipmapLevels, boolean hdr) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		this.mipmapLevels = mipmapLevels;
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		if (width < 1)
			throw new IllegalArgumentException("width cannot be < 1");
		if (height < 1)
			throw new IllegalArgumentException("height cannot be < 1");
		if (mipmapLevels < 1)
			throw new IllegalArgumentException("mipmapLevels cannot be < 1");
		
		for (int i = 0; i < mipmapLevels; i++)
			glTexImage2D(GL_TEXTURE_2D, i, hdr?GL_RGBA32F:GL_RGBA8, width/(1<<i), height/(1<<i), 0, GL_BGRA, GL_FLOAT, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, mipmapLevels-1);
	}
	
	public Texture setTextureData(InputStream s, int mipmapLevel) {
		if (mipmapLevel >= mipmapLevels)
			throw new IllegalArgumentException("mipmapLevel cannot be >= mipmapLevels");
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (img == null)
			throw new IllegalStateException("Could not load image");
		
		int scale = 1<<mipmapLevel;
		int width = img.getWidth() / scale == 0 ? 1 : img.getWidth() / scale;
		int height = img.getHeight() / scale == 0 ? 1 : img.getHeight() / scale;
		
		if (img.getWidth() != width || img.getHeight() != height)
			throw new IllegalStateException("Passed image has incorrect dimensions");
		
		float[] tex = new float[width * height * 4];
		int idx = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0 ; j < width; j++) {
				tex[idx++] = (img.getRGB(j, i) & 0xff) / 255f;
				tex[idx++] = (img.getRGB(j, i) >> 8 & 0xff) / 255f;
				tex[idx++] = (img.getRGB(j, i) >> 16 & 0xff) / 255f;
				tex[idx++] = (img.getRGB(j, i) >> 24 & 0xff) / 255f;
			}
		}
		
		glBindTexture(GL_TEXTURE_2D, id);
		glTexSubImage2D(GL_TEXTURE_2D, mipmapLevel, 0, 0, width, height, GL_BGRA, GL_FLOAT, Util.toBuffer(tex));
		
		return this;
	}
	
	@Override
	protected void finalize() {
		glDeleteTextures(id);
	}
}
