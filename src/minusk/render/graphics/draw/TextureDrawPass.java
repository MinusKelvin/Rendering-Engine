package minusk.render.graphics.draw;

import minusk.render.graphics.globjects.Shader;
import minusk.render.graphics.globjects.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL20.*;

public class TextureDrawPass extends DrawPass {
	private static Shader textureShader;
	private static int projloc;
	
	private Texture texture;
	
	public TextureDrawPass(Texture tex, int maxPolys) {
		super(maxPolys, 20, GL_STREAM_DRAW);
		if (textureShader == null) {
			textureShader = new Shader(vert, frag);
			textureShader.link();
			textureShader.use();
			projloc = glGetUniformLocation(textureShader.id, "proj");
		}
		shader = textureShader;
		cameraLocation = projloc;
		texture = tex;
	}

	public TextureDrawPass(Texture tex) {
		this(tex, DEFAULT_MAX_POLYGONS);
	}

	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 20, 12);
		glEnableVertexAttribArray(1);
		
		glBindTexture(GL_TEXTURE_2D, texture.id);
	}

	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void drawTriangle(float x1, float y1, float z1, float u1, float v1,
			float x2, float y2, float z2, float u2, float v2,
			float x3, float y3, float z3, float u3, float v3) {
		checkDraw(1);
		
		mapped.putFloat(x1);
		mapped.putFloat(y1);
		mapped.putFloat(z1);
		mapped.putFloat(u1);
		mapped.putFloat(v1);
		
		mapped.putFloat(x2);
		mapped.putFloat(y2);
		mapped.putFloat(z2);
		mapped.putFloat(u2);
		mapped.putFloat(v2);
		
		mapped.putFloat(x3);
		mapped.putFloat(y3);
		mapped.putFloat(z3);
		mapped.putFloat(u3);
		mapped.putFloat(v3);
		
		polys++;
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2,
			float x3, float y3, float u3, float v3, float z) {
		drawTriangle(x1, y1, z, u1, v1, x2, y2, z, u2, v2, x3, y3, z, u3, v3);
	}
	
	public void drawTriangle(float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2,
			float x3, float y3, float u3, float v3) {
		drawTriangle(x1, y1, u1, v1, x2, y2, u2, v2, x3, y3, u3, v3, 0);
	}
	
	public void drawRectangle(float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2) {
		drawTriangle(x1, y1, u1, v1, x1, y2, u1, v2, x2, y2, u2, v2);
		drawTriangle(x1, y1, u1, v1, x2, y2, u2, v2, x2, y1, u2, v1);
	}
	
	private static final String vert =
			  "#version 330 core							\n"
			+ "												\n"
			+ "layout(location = 0) in vec3 pos;			\n"
			+ "layout(location = 1) in vec2 tex;			\n"
			+ "												\n"
			+ "uniform mat4 proj;							\n"
			+ "												\n"
			+ "out vec2 texcoord;							\n"
			+ "												\n"
			+ "void main() {								\n"
			+ "    gl_Position = proj * vec4(pos, 1.0);		\n"
			+ "    texcoord = tex;							\n"
			+ "}											\n",
			
			frag =
			  "#version 330 core							\n"
			+ "												\n"
			+ "in vec2 texcoord;							\n"
			+ "												\n"
			+ "uniform sampler2D tex;						\n"
			+ "												\n"
			+ "out vec4 color;								\n"
			+ "												\n"
			+ "void main() {								\n"
			+ "    color = texture(tex, texcoord);			\n"
			+ "}											\n";
}
