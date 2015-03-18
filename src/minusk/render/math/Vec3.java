package minusk.render.math;

public class Vec3 {
	public float x,y,z;
	
	public Vec3() {
		set(0,0,0);
	}
	
	public Vec3(float x, float y, float z) {
		set(x,y,z);
	}
	
	public Vec3(Vec3 copy) {
		set(copy);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vec3 orig) {
		set(orig.x, orig.y, orig.z);
	}
	
	public void normalize() {
		float len = length();
		set(x / len, y / len, z / len);
	}
	
	public float lengthsq() {
		return x*x + y*y + z*z;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthsq());
	}
}
