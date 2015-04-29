package minusk.render.graphics.globjects;

import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_STENCIL_ATTACHMENT;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.util.Arrays;

import minusk.render.util.Util;

public class Framebuffer {
	private static int defaultWidth, defaultHeight;
	private final int id;
	private int viewWidth, viewHeight;
	private Texture[] colorAttachments = new Texture[16];
	private DepthStencilBuffer depthStencil;
	private int[] activeDrawBufs = new int[0];
	
	/**
	 * Creates a new <code>Framebuffer</code> for use with off-screen rendering.
	 * This does not create anything to render to.
	 * The user must attach textures with {@link attachTexture}
	 * and attach depth-stencil buffers with {@link setDepthStencil}.
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
				glClearBufferfv(GL_COLOR, i-GL_COLOR_ATTACHMENT0, Util.toBuffer(new float[] {0,0,0,0}));
		glClearBufferfv(GL_DEPTH, 0, Util.toBuffer(new float[] {0}));
	}
	
	public Texture attachTextures(Texture tex, int index, int mipmapLevel) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		if (tex == null) {
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, 0, 0);
			if (colorAttachments[index] != null) {
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
			if (colorAttachments[index] == null) {
				activeDrawBufs = Arrays.copyOf(activeDrawBufs, activeDrawBufs.length+1);
				activeDrawBufs[activeDrawBufs.length-1] = GL_COLOR_ATTACHMENT0 + index;
			}
		}
		glDrawBuffers(Util.toBuffer(activeDrawBufs));
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		Texture old = colorAttachments[index];
		colorAttachments[index] = tex;
		return old;
	}
	
	public DepthStencilBuffer setDepthStencil(DepthStencilBuffer buf) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		DepthStencilBuffer old = depthStencil;
		depthStencil = buf;
		return old;
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
	protected void finalize() {
		glDeleteFramebuffers(id);
	}
}
