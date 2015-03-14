package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import minusk.render.graphics.Color;
import minusk.render.util.Shader;


public class ColorDrawPass extends DrawPass {
	private static Shader colorShader;
	private static int projloc;
	
	public ColorDrawPass(int maxPolygons) {
		super(maxPolygons, 16);
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
		this(DEFAULT_MAX_POLYGONS);
	}

	@Override
	protected void preRender() {
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, 16, 12);
		glEnableVertexAttribArray(1);
	}

	@Override
	protected void postRender() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public void drawTriangle(float x1, float y1, float z1, int color1,
			float x2, float y2, float z2, int color2,
			float x3, float y3, float z3, int color3) {
		checkDraw(3);
		
		mapped.putFloat(x1);
		mapped.putFloat(y1);
		mapped.putFloat(z1);
		mapped.putInt(color1);
		
		mapped.putFloat(x2);
		mapped.putFloat(y2);
		mapped.putFloat(z2);
		mapped.putInt(color2);
		
		mapped.putFloat(x3);
		mapped.putFloat(y3);
		mapped.putFloat(z3);
		mapped.putInt(color3);
		
		polys++;
	}
	
	public void drawTriangle(float x1, float y1, float z1, Color color1,
			float x2, float y2, float z2, Color color2,
			float x3, float y3, float z3, Color color3) {
		drawTriangle(x1, y1, z1, color1.intValue(), x2, y2, z2, color2.intValue(), x3, y3, z3, color3.intValue());
	}
	
	public void drawTriangle(float x1, float y1, Color color1,
			float x2, float y2, Color color2,
			float x3, float y3, Color color3, float z) {
		drawTriangle(x1, y1, z, color1.intValue(), x2, y2, z, color2.intValue(), x3, y3, z, color3.intValue());
	}
	
	public void drawTriangle(float x1, float y1, int color1,
			float x2, float y2, int color2,
			float x3, float y3, int color3, float z) {
		drawTriangle(x1, y1, z, color1, x2, y2, z, color2, x3, y3, z, color3);
	}
	
	public void drawTriangle(float x1, float y1, Color color1,
			float x2, float y2, Color color2,
			float x3, float y3, Color color3) {
		drawTriangle(x1, y1, 0, color1.intValue(), x2, y2, 0, color2.intValue(), x3, y3, 0, color3.intValue());
	}
	
	public void drawTriangle(float x1, float y1, int color1,
			float x2, float y2, int color2,
			float x3, float y3, int color3) {
		drawTriangle(x1, y1, 0, color1, x2, y2, 0, color2, x3, y3, 0, color3);
	}
	
	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
		drawTriangle(x1, y1, color, x2, y2, color, x3, y3, color);
	}
	
	public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
		drawTriangle(x1, y1, x2, y2, x3, y3, color.intValue());
	}
	
	public void drawRectangle(float x1, float y1, float x2, float y2, int color, float z) {
		drawTriangle(x1, y1, color, x1, y2, color, x2, y2, color, z);
		drawTriangle(x1, y1, color, x2, y2, color, x2, y1, color, z);
	}
	
	public void drawRectangle(float x1, float y1, float x2, float y2, Color color, float z) {
		drawRectangle(x1, y1, x2, y2, color.intValue(), z);
	}
	
	public void drawRectangle(float x1, float y1, float x2, float y2, int color) {
		drawRectangle(x1, y1, x2, y2, color, 0);
	}
	
	public void drawRectangle(float x1, float y1, float x2, float y2, Color color) {
		drawRectangle(x1, y1, x2, y2, color.intValue());
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
