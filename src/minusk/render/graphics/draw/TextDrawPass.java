package minusk.render.graphics.draw;

import minusk.render.graphics.filters.BlendFunc;
import minusk.render.graphics.globjects.Shader;
import minusk.render.graphics.globjects.SpriteSheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

/**
 * Created by MinusKelvin on 25/08/15.
 */
public class TextDrawPass extends DrawPass {
	private static final Pattern splitPattern = Pattern.compile("(\\S+)|([^\\S\n]+|\\n)");
	
	private static Shader textShader;
	private static int projloc;
	
	private final SpriteSheet text;
	private final float[] characterSizes;
	private final float charWidth;
	private final float charHeight;
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the specified parameters.
	 * 
	 * @param width - Width of the characters
	 * @param height - Height of the characters
	 * @param text - Bitmap font that this <code>TextDrawPass</code> will render
	 * @param characterLengths - Used to make text non-monospace. Values should be in range 0-1.
	 */
	public TextDrawPass(float width, float height, SpriteSheet text, float[] characterLengths) {
		super(DEFAULT_MAX_POLYGONS, 28);
		if (textShader == null) {
			textShader = new Shader(getClass().getResourceAsStream("/minusk/render/graphics/draw/shaders/texarray-color-pos.vs.glsl"),
					getClass().getResourceAsStream("/minusk/render/graphics/draw/shaders/texarray-color.fs.glsl"));
			textShader.link();
			textShader.use();
			projloc = glGetUniformLocation(textShader.id, "proj");
		}
		shader = textShader;
		cameraLocation = projloc;
		this.text = text;
		glBindTexture(GL_TEXTURE_2D_ARRAY, this.text.id);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		this.characterSizes = characterLengths;
		charWidth = width;
		charHeight = height;
		setBlendFunc(BlendFunc.TRANSPARENCY);
	}
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the specified bitmap font and character size equal to the maximum
	 * size of a the supplied font's characters in pixels.
	 * 
	 * @param text - The bitmap font
	 * @param characterLengths - Used to make text non-monospace. Values should be in range 0-1.
	 */
	public TextDrawPass(SpriteSheet text, float[] characterLengths) {
		this(text.width, text.height, text, characterLengths);
	}
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the specified bitmap font and with the specified character size.
	 * This is equivalent to <code>new TextDrawPass(size * fontAspectRatio, size, text, characterLengths)</code>.
	 *
	 * @param size - The size of the characters
	 * @param text - The bitmap font
	 * @param characterLengths - Used to make text non-monospace. Values should be in range 0-1.
	 */
	public TextDrawPass(float size, SpriteSheet text, float[] characterLengths) {
		this(size * ((float) text.width / text.height), size, text, characterLengths);
	}
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the default bitmap font, its default character lengths, and with
	 * the specified character size.
	 * 
	 * @param width - Width of the characters
	 * @param height - Height of the characters
	 */
	public TextDrawPass(float width, float height) {
		this(width, height, getDefaultText(), defaultCharacterLengths);
	}
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the default bitmap font, its default character lengths, and with
	 * the specified character size. This is equivalent to <code>new TextDrawPass(size * fontAspectRatio, size)</code>.
	 * 
	 * @param size - The size of the characters
	 */
	public TextDrawPass(float size) {
		this(size, getDefaultText(), defaultCharacterLengths);
	}
	
	/**
	 * Initializes this <code>TextDrawPass</code> with the default bitmap font, its default character lengths, and
	 * character size equal to the maximum size of a the default font's characters in pixels.
	 */
	public TextDrawPass() {
		this(getDefaultText().height);
	}
	
	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 28, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 28, 12);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 4, GL_UNSIGNED_BYTE, true, 28, 24);
		glEnableVertexAttribArray(2);
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, text.id);
	}
	
	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}
	
	private void drawCharacter(float x, float y, char c, int color, float scale) {
		checkDraw(2);
		
		mapped.putFloat(x);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putFloat(0);
		mapped.putFloat(1);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		mapped.putFloat(x);
		mapped.putFloat(y+charHeight*scale);
		mapped.putFloat(0);
		mapped.putFloat(0);
		mapped.putFloat(0);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		mapped.putFloat(x+charWidth*scale);
		mapped.putFloat(y+charHeight*scale);
		mapped.putFloat(0);
		mapped.putFloat(1);
		mapped.putFloat(0);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		mapped.putFloat(x);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putFloat(0);
		mapped.putFloat(1);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		mapped.putFloat(x+charWidth*scale);
		mapped.putFloat(y+charHeight*scale);
		mapped.putFloat(0);
		mapped.putFloat(1);
		mapped.putFloat(0);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		mapped.putFloat(x+charWidth*scale);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putFloat(1);
		mapped.putFloat(1);
		mapped.putFloat(c);
		mapped.putInt(color);
		
		polys += 2;
	}
	
	public void drawString(float x, float y, String text, int color, float scale, float wrapPoint, boolean ydown) {
		final float origx = x;
		float distx = 0;
		Matcher str = splitPattern.matcher(text);
		for (;str.find();) {
			String word = str.group();
			float wordlen = getLength(word);
			if ((wrapPoint != 0 && distx + wordlen > wrapPoint && distx != 0) || word.endsWith("\n")) {
				if (str.group(2) != null) {
					for (int i = 0; i < word.length(); i++) {
						drawCharacter(x,y,word.charAt(i),color,scale);
						x += characterSizes[word.charAt(i)] * charWidth * scale;
					}
					distx = 0;
					y += (ydown?1:-1) * charHeight * 1.15f * scale;
					x = origx;
					continue;
				} else {
					distx = 0;
					y += (ydown?1:-1) * charHeight * 1.15f * scale;
					x = origx;
				}
			}
			distx += wordlen;
			for (int i = 0; i < word.length(); i++) {
				drawCharacter(x,y,word.charAt(i),color,scale);
				x += characterSizes[word.charAt(i)] * charWidth * scale;
			}
		}
	}
	
	private float getLength(String word) {
		float l = 0;
		for (int i = 0; i < word.length(); i++) {
			l += characterSizes[word.charAt(i)] * charWidth;
		}
		return l;
	}
	
	private static SpriteSheet defaultText;
	private static SpriteSheet getDefaultText() {
		if (defaultText == null)
			defaultText = new SpriteSheet(20,24,256,1).setSprites(TextDrawPass.class.getResourceAsStream("/minusk/render/graphics/draw/bmpfonts/default.png"),0,0);
		return defaultText;
	}
	
	private static float[] defaultCharacterLengths = {
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.2f, 0.4f,  0.6f,  0.6f, 0.6f, 0.6f,  0.2f, 0.5f,  0.5f,  0.4f, 0.6f, 0.2f, 0.6f, 0.2f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.2f, 0.2f, 0.6f, 0.6f, 0.6f, 0.6f,
			1.0f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.7f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.8f, 0.6f,  0.6f,  0.6f, 0.5f, 0.6f, 0.5f, 0.6f, 0.6f,
			0.3f, 0.6f, 0.6f,  0.6f,  0.6f, 0.5f, 0.6f,  0.6f, 0.6f,  0.2f,  0.5f, 0.6f, 0.2f, 0.8f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.5f,  0.5f, 0.6f, 0.6f,  0.8f, 0.6f,  0.6f,  0.5f, 0.5f, 0.2f, 0.5f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.2f, 0.6f,  0.7f,  0.7f, 0.6f, 0.2f,  0.6f, 0.4f,  1.0f,  0.6f, 0.7f, 0.6f, 0.6f, 1.0f, 0.6f,
			0.5f, 0.6f, 0.35f, 0.35f, 0.3f, 0.6f, 0.55f, 0.3f, 0.35f, 0.35f, 0.6f, 0.7f, 0.7f, 0.7f, 0.7f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 1.0f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.85f, 0.6f, 0.5f,  0.5f,  0.5f, 0.5f, 0.3f, 0.3f, 0.4f, 0.4f,
			0.6f, 0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f,  0.6f, 0.6f,  0.6f,  0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f,
	};
}
