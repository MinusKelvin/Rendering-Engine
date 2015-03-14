package minusk.render.graphics;


import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12.glTexImage3D;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import minusk.render.util.Util;

public class Texture2DArray {
	public final int width, height, mipmapLevels, id, layers;
	
	private boolean[][] completeness;
	private boolean complete;
	
	public Texture2DArray(int width, int height, int layers, int mipmapLevels) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		this.layers = layers;
		this.mipmapLevels = mipmapLevels;
		
		completeness = new boolean[mipmapLevels+1][layers];
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
		
		if (width < 1 || height < 1 || layers < 1)
			System.err.println("Can't allocate a texture with less than 1 width, height, or layer count.");
		else {
			glTexStorage3D(GL_TEXTURE_2D_ARRAY, mipmapLevels, GL_RGBA32F, width, height, layers);
			completeness[mipmapLevels] = new boolean[] {true};
		}
	}
	
	public Texture2DArray(int width, int height, int layers, int mipmapLevels, boolean compatibilityMode) {
		id = glGenTextures();
		this.width = width;
		this.height = height;
		this.layers = layers;
		this.mipmapLevels = mipmapLevels;
		
		completeness = new boolean[mipmapLevels+1][layers];
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, id);
		
		if (width < 1 || height < 1 || layers < 1)
			System.err.println("Can't allocate a texture with less than 1 width, height, or layer count.");
		else {
			for (int i = 0; i < mipmapLevels; i++)
				glTexImage3D(GL_TEXTURE_2D_ARRAY, i, GL_RGBA32F, width/(1<<i), height/(1<<i), layers, 0, GL_BGRA, GL_FLOAT, 0);
			glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAX_LEVEL, mipmapLevels-1);
			completeness[mipmapLevels] = new boolean[] {true};
		}
	}
	
	public Texture2DArray setTextureData(InputStream data, int layer, int mipmapLevel) {
		return setTextureData(data,layer,mipmapLevel,true);
	}
	
	public Texture2DArray setTextureData(InputStream data, int layer, int mipmapLevel, boolean leftfirst) {
		if (!completeness[mipmapLevels][0]) {
			System.err.println("Attempted to set the data of a texture that is invalid.");
			return this;
		}
		if (mipmapLevel > mipmapLevels) {
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
		glTexSubImage3D(GL_TEXTURE_2D_ARRAY, mipmapLevel, 0, 0, layer, width, height,
				leftRepeats * downRepeats, GL_BGRA, GL_FLOAT, Util.toBuffer(tex));
		
		for (int i = layer; i < leftRepeats * downRepeats; i++)
			completeness[mipmapLevel][i] = true;
		complete = Util.all(completeness);
		return this;
	}
	
	public boolean isComplete() {
		return complete;
	}
}
