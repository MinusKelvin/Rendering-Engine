package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;

import minusk.render.graphics.Texture2DArray;
import minusk.render.util.Shader;

public class TexturedDrawPass extends DrawPass {
	private static Shader textureShader;
	private static int projloc;
	
	public final Texture2DArray texture;

	public TexturedDrawPass(int maxPolys, Texture2DArray texture) {
		super(maxPolys, 24);
		if (textureShader == null) {
			textureShader = new Shader(vert, frag);
			textureShader.link();
			textureShader.use();
			projloc = glGetUniformLocation(textureShader.id, "proj");
		}
		shader = textureShader;
		cameraLocation = projloc;
		this.texture = texture;
	}

	public TexturedDrawPass(Texture2DArray texture) {
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
		checkDraw(3);
		
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
