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
		return (float) Math.sqrt(length());
	}
}
