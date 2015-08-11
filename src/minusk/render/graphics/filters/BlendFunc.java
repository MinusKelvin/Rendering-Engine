package minusk.render.graphics.filters;

import static org.lwjgl.opengl.GL11.GL_DST_ALPHA;
import static org.lwjgl.opengl.GL11.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_ZERO;

public class BlendFunc {
	public static final BlendFunc OVERWRITE = new BlendFunc(BlendOption.ONE, BlendOption.ZERO);
	public static final BlendFunc TRANSPARENCY = new BlendFunc(BlendOption.SRC_ALPHA, BlendOption.ONE_MINUS_SRC_ALPHA);
	public static final BlendFunc ADDITIVE = new BlendFunc(BlendOption.ONE, BlendOption.ONE);
	public static final BlendFunc MULTIPLY = new BlendFunc(BlendOption.DST_COLOR, BlendOption.ZERO);
	
	public final BlendOption src, dst;
	
	public BlendFunc(BlendOption src, BlendOption dst) {
		this.src = src;
		this.dst = dst;
	}
	
	public enum BlendOption {
		ZERO(GL_ZERO),
		ONE(GL_ONE),
		ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA),
		ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
		ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
		ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
		SRC_ALPHA(GL_SRC_ALPHA),
		SRC_COLOR(GL_SRC_COLOR),
		DST_ALPHA(GL_DST_ALPHA),
		DST_COLOR(GL_DST_COLOR);
		
		public final int glenum;
		BlendOption(int glenum) {
			this.glenum = glenum;
		}
	}
}
