package minusk.render.graphics.draw;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import minusk.render.graphics.Color;
import minusk.render.graphics.globjects.Shader;
import minusk.render.math.Matrix2;
import minusk.render.math.Vec2;


public class ColorDrawPass extends DrawPass {
	private static Shader colorShader;
	private static int projloc;
	
	static Shader getColorShader() {
		if (colorShader == null) {
			colorShader = new Shader(ColorDrawPass.class.getResourceAsStream("/minusk/render/graphics/draw/shaders/color.vs"),
					ColorDrawPass.class.getResourceAsStream("/minusk/render/graphics/draw/shaders/color.fs"));
			colorShader.link();
			colorShader.use();
			projloc = glGetUniformLocation(colorShader.id, "proj");
		}
		return colorShader;
	}
	
	static int getProjloc() {
		return projloc;
	}
	
	public ColorDrawPass(int maxPolygons) {
		super(maxPolygons, 16);
		shader = getColorShader();
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
		checkDraw(1);
		
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
	
	public void drawLine(float x1, float y1, float x2, float y2, float width, Color c) {
		drawLine(x1,y1,x2,y2,width,c.intValue());
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, float width, int color) {
		drawLine(x1, y1, color, width, x2, y2, color, width);
	}
	
	public void drawLine(float x1, float y1, int color1, float width1, float x2, float y2, int color2, float width2) {
		Vec2 vec = new Vec2(x1-x2, y1-y2);
		vec.normalize();
		vec.x /= 2;
		vec.y /= 2;
		vec.transform(new Matrix2(0,-1,1,0));
		drawTriangle(vec.x*width1 + x1, vec.y*width1 + y1, color1,
				-vec.x*width1 + x1, -vec.y*width1 + y1, color1,
				vec.x*width2 + x2, vec.y*width2 + y2, color2);
		drawTriangle(vec.x*width2 + x2, vec.y*width2 + y2, color2,
				-vec.x*width2 + x2, -vec.y*width2 + y2, color2,
				-vec.x*width1 + x1, -vec.y*width1 + y1, color1);
	}
}
