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
	
	public void transform(Matrix3 mat) {
		float tx = mat.m00 * x + mat.m10 * y + mat.m20 * z;
		float ty = mat.m01 * x + mat.m11 * y + mat.m21 * z;
		float tz = mat.m02 * x + mat.m12 * y + mat.m22 * z;
		x = tx;
		y = ty;
		z = tz;
	}
}
