package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import minusk.render.graphics.filters.BlendFunc;
import minusk.render.util.Shader;


public class ColorDrawPass extends DrawPass {
	private static Shader colorShader;
	private static int projloc;
	
	public ColorDrawPass(int maxVerticies) {
		super(maxVerticies, 16);
		if (colorShader == null) {
			colorShader = new Shader(vert, frag);
			colorShader.link();
			colorShader.use();
			projloc = glGetUniformLocation(colorShader.id, "proj");
		}
		shader = colorShader;
		cameraLocation = projloc;
	}
	
	public ColorDrawPass() {
		this(DEFAULT_MAX_VERTICIES);
	}

	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, false, 16, 12);
		glEnableVertexAttribArray(1);
	}

	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void drawRectangle(float x, float y, float w, float h, int color) {
		checkDraw(6);
		
		mapped.putFloat(x);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		mapped.putFloat(x+w);
		mapped.putFloat(y+h);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		mapped.putFloat(x+w);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		mapped.putFloat(x);
		mapped.putFloat(y);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		mapped.putFloat(x);
		mapped.putFloat(y+h);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		mapped.putFloat(x+w);
		mapped.putFloat(y+h);
		mapped.putFloat(0);
		mapped.putInt(color);
		
		verticies+=6;
	}
	
	private static final String vert = 
			  "#version 330 core                           \n"
			+ "                                            \n"
			+ "layout(location = 0) in vec3 pos;           \n"
			+ "layout(location = 1) in vec4 color;         \n"
			+ "                                            \n"
			+ "uniform mat4 proj;                          \n"
			+ "                                            \n"
			+ "out vec4 col;                               \n"
			+ "                                            \n"
			+ "void main() {                               \n"
			+ "    gl_Position = proj * vec4(pos, 1.0);    \n"
			+ "    col = color;                            \n"
			+ "}                                           \n",
			  
			frag =
			  "#version 330 core                           \n"
			+ "                                            \n"
			+ "in vec4 col;                                \n"
			+ "                                            \n"
			+ "out vec4 color;                             \n"
			+ "                                            \n"
			+ "void main() {                               \n"
			+ "    color = col;                            \n"
			+ "}                                           \n";
}
