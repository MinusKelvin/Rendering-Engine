package minusk.render.math;

public class Vec2 {
	public float x,y;
	
	public Vec2() {
		set(0,0);
	}
	
	public Vec2(float x, float y) {
		set(x,y);
	}
	
	public Vec2(Vec2 copy) {
		set(copy);
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Vec2 orig) {
		set(orig.x, orig.y);
	}
	
	public void normalize() {
		float len = length();
		set(x / len, y / len);
	}
	
	public float lengthsq() {
		return x*x + y*y;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthsq());
	}
	
	public float angle(){
		return (float) Math.atan2(y, x);
	}
	
	public void transform(Matrix2 mat) {
		float tx = mat.m00 * x + mat.m10 * y;
		float ty = mat.m01 * x + mat.m11 * y;
		x = tx;
		y = ty;
	}

	public void transform(Matrix3 mat) {
		Vec3 v = new Vec3(x, y, 1);
		v.transform(mat);
		set(v.x / v.z, v.y / v.z);
	}
	
	public void add(Vec2 other) {
		x += other.x;
		y += other.y;
	}
	
	public void sub(Vec2 other) {
		x -= other.x;
		y -= other.y;
	}
	
	public void scale(Vec2 factor) {
		x *= factor.x;
		y *= factor.y;
	}
	
	public void scale(float factor) {
		x *= factor;
		y *= factor;
	}
	
	public float dot(Vec2 other) {
		return x*other.x + y*other.y;
	}
}
