package minusk.render.graphics.globjects;


import minusk.render.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

public class SpriteSheet {
	public final int width, height, mipmapLevels, id, spriteCount;
	
	public SpriteSheet(int width, int height, int spriteCount, int mipmapLevels) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		this.spriteCount = spriteCount;
		this.mipmapLevels = mipmapLevels;
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
		
		if (width < 1)
			throw new IllegalArgumentException("width cannot be < 1");
		if (height < 1)
			throw new IllegalArgumentException("height cannot be < 1");
		if (mipmapLevels < 1)
			throw new IllegalArgumentException("mipmapLevels cannot be < 1");
		if (spriteCount < 1)
			throw new IllegalArgumentException("spriteCount cannot be < 1");
		
		for (int i = 0; i < mipmapLevels; i++)
			glTexImage3D(GL_TEXTURE_2D_ARRAY, i, GL_RGBA8, width/(1<<i), height/(1<<i), spriteCount, 0, GL_BGRA, GL_FLOAT, 0);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAX_LEVEL, mipmapLevels-1);
	}
	
	public SpriteSheet setSprites(InputStream data, int firstSprite, int mipmapLevel) {
		return setSprites(data,firstSprite,mipmapLevel,true);
	}
	
	public SpriteSheet setSprites(InputStream data, int firstSprite, int mipmapLevel, boolean leftfirst) {
		if (mipmapLevel >= mipmapLevels) {
			System.err.println("Attempted to set the data of a texture at a mipmap level higher than the highest specified.");
			return this;
		}
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (image == null)
			return this;
		
		int scale = 1 << mipmapLevel;
		int width = this.width / scale == 0 ? 1 : this.width / scale;
		int height = this.height / scale == 0 ? 1 : this.height / scale;
		
		if (image.getWidth() < width || image.getHeight() < height) {
			System.err.println("Attempted to set texture data that is too small for the specified mipmap level.");
			return this;
		}
		
		int leftRepeats = image.getWidth() / width;
		int downRepeats = image.getHeight() / height;
		
		float[] tex = new float[width * height * 4 * leftRepeats * downRepeats];
		int idx = 0;
		if (leftfirst) {
			for (int k = 0; k < downRepeats; k++) {
				for (int l = 0; l < leftRepeats; l++) {
					for (int i = 0; i < height; i++) {
						for (int j = 0; j < width; j++) {
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 8 & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 16 & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 24 & 0xff) / 255f;
						}
					}
				}
			}
		} else {
			for (int l = 0; l < leftRepeats; l++) {
				for (int k = 0; k < downRepeats; k++) {
					for (int i = 0; i < height; i++) {
						for (int j = 0; j < width; j++) {
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 8 & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 16 & 0xff) / 255f;
							tex[idx++] = (image.getRGB(j + l * width, i + k * height) >> 24 & 0xff) / 255f;
						}
					}
				}
			}
		}
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
		glTexSubImage3D(GL_TEXTURE_2D_ARRAY, mipmapLevel, 0, 0, firstSprite, width, height,
				leftRepeats * downRepeats, GL_BGRA, GL_FLOAT, Util.toBuffer(tex));
		
		return this;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		glDeleteTextures(id);
	}
}
