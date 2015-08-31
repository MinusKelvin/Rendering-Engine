package minusk.render.graphics.draw;

import minusk.render.graphics.globjects.Shader;
import minusk.render.graphics.globjects.SpriteSheet;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

public class SpriteDrawPass extends DrawPass {
	private static Shader spriteShader;
	private static int projloc;
	
	public final SpriteSheet texture;

	public SpriteDrawPass(int maxPolys, SpriteSheet texture) {
		super(maxPolys, 24);
		shader = getSpriteShader();
		cameraLocation = getSpriteProjloc();
		this.texture = texture;
	}

	public SpriteDrawPass(SpriteSheet texture) {
		this(DEFAULT_MAX_POLYGONS, texture);
	}

	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 24, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 24, 12);
		glEnableVertexAttribArray(1);
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, texture.id);
	}

	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void drawTriangle(float x1, float y1, float z1, float u1, float v1, float layer1,
			float x2, float y2, float z2, float u2, float v2, float layer2,
			float x3, float y3, float z3, float u3, float v3, float layer3) {
		checkDraw(1);
		
		mapped.putFloat(x1);
		mapped.putFloat(y1);
		mapped.putFloat(z1);
		mapped.putFloat(u1);
		mapped.putFloat(v1);
		mapped.putFloat(layer1);
		
		mapped.putFloat(x2);
		mapped.putFloat(y2);
		mapped.putFloat(z2);
		mapped.putFloat(u2);
		mapped.putFloat(v2);
		mapped.putFloat(layer2);
		
		mapped.putFloat(x3);
		mapped.putFloat(y3);
		mapped.putFloat(z3);
		mapped.putFloat(u3);
		mapped.putFloat(v3);
		mapped.putFloat(layer3);
		
		polys++;
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1, float layer1,
			float x2, float y2, float u2, float v2, float layer2,
			float x3, float y3, float u3, float v3, float layer3, float z) {
		drawTriangle(x1, y1, z, u1, v1, layer1, x2, y2, z, u2, v2, layer2, x3, y3, z, u3, v3, layer3);
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1, float layer1,
			float x2, float y2, float u2, float v2, float layer2,
			float x3, float y3, float u3, float v3, float layer3) {
		drawTriangle(x1, y1, u1, v1, layer1, x2, y2, u2, v2, layer2, x3, y3, u3, v3, layer3, 0);
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2,
			float x3, float y3, float u3, float v3, float layer) {
		drawTriangle(x1, y1, u1, v1, layer, x2, y2, u2, v2, layer, x3, y3, u3, v3, layer);
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2,
			float x3, float y3, float u3, float v3, float layer, float z) {
		drawTriangle(x1, y1, z, u1, v1, layer, x2, y2, z, u2, v2, layer, x3, y3, z, u3, v3, layer);
	}
	
	public void drawRectangle(float x1, float y1, float u1, float v1, float layer1,
			float x2, float y2, float u2, float v2, float layer2) {
		drawTriangle(x1, y1, u1, v1, layer1, x1, y2, u1, v2, (layer1+layer2)/2, x2, y2, u2, v2, layer2);
		drawTriangle(x1, y1, u1, v1, layer1, x2, y2, u2, v2, layer2, x2, y1, u2, v1, (layer1+layer2)/2);
	}
	
	public void drawSprite(float x, float y, float spriteSize, int index) {
		drawRectangle(x, y, 0, 1, index, x + spriteSize, y + spriteSize, 1, 0, index);
	}
	
	public static Shader getSpriteShader() {
		if (spriteShader == null) {
			spriteShader = new Shader(vert, frag);
			spriteShader.link();
			spriteShader.use();
			projloc = glGetUniformLocation(spriteShader.id, "proj");
		}
		return spriteShader;
	}
	
	public static int getSpriteProjloc() {
		if (spriteShader == null) {
			spriteShader = new Shader(vert, frag);
			spriteShader.link();
			spriteShader.use();
			projloc = glGetUniformLocation(spriteShader.id, "proj");
		}
		return projloc;
	}
	
	private static final String vert =
			  "#version 330 core							\n"
			+ "												\n"
			+ "layout(location = 0) in vec3 pos;			\n"
			+ "layout(location = 1) in vec3 tex;			\n"
			+ "												\n"
			+ "uniform mat4 proj;							\n"
			+ "												\n"
			+ "out vec3 texcoord;							\n"
			+ "												\n"
			+ "void main() {								\n"
			+ "    gl_Position = proj * vec4(pos, 1.0);		\n"
			+ "    texcoord = tex;							\n"
			+ "}											\n";
	
	private static final String frag =
			  "#version 330 core							\n"
			+ "												\n"
			+ "in vec3 texcoord;							\n"
			+ "												\n"
			+ "uniform sampler2DArray textures;				\n"
			+ "												\n"
			+ "out vec4 color;								\n"
			+ "												\n"
			+ "void main() {								\n"
			+ "    color = texture(textures, texcoord);		\n"
			+ "}											\n";
}
