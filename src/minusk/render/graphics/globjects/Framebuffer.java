package minusk.render.graphics.globjects;

import minusk.render.graphics.Color;
import minusk.render.interfaces.Disposable;
import minusk.render.util.Util;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class Framebuffer implements Disposable {
	private static int defaultWidth, defaultHeight;
	public final int id;
	private int viewWidth, viewHeight;
	private Texture[] colorAttachments0 = new Texture[16];
	private MultisampledTexture[] colorAttachments1 = new MultisampledTexture[16];
	private DepthStencilBuffer depthStencil;
	private int[] activeDrawBufs = new int[0];
	private Color clearColor = Color.Transparent_Black;
	private boolean disposed = false;
	
	/**
	 * Creates a new <code>Framebuffer</code> for use with off-screen rendering.
	 * This does not create anything to render to.
	 * The user must attach textures with <code>attachTextures</code>
	 * and attach depth-stencil buffers with <code>setDepthStencil</code>.
	 * 
	 * @param width The width of the viewport for this <code>Framebuffer</code>
	 * @param height The height of the viewport for this <code>Framebuffer</code>
	 */
	public Framebuffer(int width, int height) {
		viewWidth = width;
		viewHeight = height;
		id = glGenFramebuffers();
	}
	
	public void use() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glViewport(0,0,viewWidth,viewHeight);
		for (int i : activeDrawBufs)
			if (i != GL_DEPTH_ATTACHMENT && i != GL_STENCIL_ATTACHMENT)
				glClearBufferfv(GL_COLOR, i-GL_COLOR_ATTACHMENT0, Util.toBuffer(
						new float[] {clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA()}));
		glClearBufferfv(GL_DEPTH, 0, Util.toBuffer(new float[] {1}));
	}
	
	public void attachTextures(Texture tex, int index, int mipmapLevel) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		if (tex == null) {
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, 0, 0);
			if (colorAttachments0[index] != null || colorAttachments1[index] != null) {
				for (int i = 0; i < activeDrawBufs.length; i++) {
					if (activeDrawBufs[i] == GL_COLOR_ATTACHMENT0 + index) {
						int[] newbufs = new int[activeDrawBufs.length-1];
						for (int j = 0; j < activeDrawBufs.length; j++) {
							if (j > i)
								newbufs[j-1] = activeDrawBufs[j];
							else if (j < i)
								newbufs[j] = activeDrawBufs[j];
						}
						activeDrawBufs = newbufs;
						break;
					}
				}
			}
		} else {
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, tex.id, mipmapLevel);
			if (colorAttachments0[index] == null && colorAttachments1[index] == null) {
				activeDrawBufs = Arrays.copyOf(activeDrawBufs, activeDrawBufs.length+1);
				activeDrawBufs[activeDrawBufs.length-1] = GL_COLOR_ATTACHMENT0 + index;
			}
		}
		glDrawBuffers(Util.toBuffer(activeDrawBufs));
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		colorAttachments0[index] = tex;
		colorAttachments1[index] = null;
	}

	public void attachTextures(MultisampledTexture tex, int index) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		if (tex == null) {
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, 0, 0);
			if (colorAttachments0[index] != null || colorAttachments1[index] != null) {
				for (int i = 0; i < activeDrawBufs.length; i++) {
					if (activeDrawBufs[i] == GL_COLOR_ATTACHMENT0 + index) {
						int[] newbufs = new int[activeDrawBufs.length-1];
						for (int j = 0; j < activeDrawBufs.length; j++) {
							if (j > i)
								newbufs[j-1] = activeDrawBufs[j];
							else if (j < i)
								newbufs[j] = activeDrawBufs[j];
						}
						activeDrawBufs = newbufs;
						break;
					}
				}
			}
		} else {
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, tex.id, 0);
			if (colorAttachments0[index] == null && colorAttachments1[index] == null) {
				activeDrawBufs = Arrays.copyOf(activeDrawBufs, activeDrawBufs.length+1);
				activeDrawBufs[activeDrawBufs.length-1] = GL_COLOR_ATTACHMENT0 + index;
			}
		}
		glDrawBuffers(Util.toBuffer(activeDrawBufs));
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		colorAttachments0[index] = null;
		colorAttachments1[index] = tex;
	}
	
	public DepthStencilBuffer setDepthStencil(DepthStencilBuffer buf) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		DepthStencilBuffer old = depthStencil;
		depthStencil = buf;
		return old;
	}
	
	public void setClearColor(Color color) {
		clearColor = color;
	}
	
	public static void useDefaultFramebuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0,0,defaultWidth,defaultHeight);
	}
	
	public static void setDefaultFramebufferDimensions(int width, int height) {
		defaultWidth = width;
		defaultHeight = height;
	}
	
	@Override
	public void dispose() {
		if (!disposed) {
			glDeleteFramebuffers(id);
			disposed = true;
		}
	}
	
	@Override
	public boolean isDisposed() {
		return disposed;
	}
}
