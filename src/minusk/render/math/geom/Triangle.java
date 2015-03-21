package minusk.render.math.geom;

import minusk.render.math.Matrix3;
import minusk.render.math.Vec2;

public class Triangle implements Shape2D {
	private Vec2[] vecs = new Vec2[3];
	{
		vecs[0] = new Vec2();
		vecs[1] = new Vec2();
		vecs[2] = new Vec2();
	}
	
	public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		set(x1,y1,x2,y2,x3,y3);
	}

	@Override
	public Vec2[] getVerticies() {
		return vecs;
	}

	@Override
	public float area() {
		Vec2 edge = new Vec2(vecs[0]);
		edge.sub(vecs[1]);
		float base = edge.length();
		edge.scale(1/base);
		Vec2 proj = new Vec2(vecs[2]);
		proj.sub(vecs[1]);
		edge.scale(proj.dot(edge));
		proj.sub(edge);
		float area = proj.length() * base / 2;
		if (area == Float.NaN)
			return 0;
		return area;
	}

	@Override
	public void transform(Matrix3 mat) {
		vecs[0].transform(mat);
		vecs[1].transform(mat);
		vecs[2].transform(mat);
	}
	
	public void set(float x1,float y1, float x2, float y2, float x3, float y3) {
		vecs[0].x = x1;
		vecs[0].y = y1;
		vecs[1].x = x2;
		vecs[1].y = y2;
		vecs[2].x = x3;
		vecs[2].y = y3;
	}
}
