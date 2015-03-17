package minusk.render.math;

public class Vec4 {
	public float x,y,z,w;
	
	public Vec4() {
		set(0,0,0,1);
	}
	
	public Vec4(float x, float y, float z, float w) {
		set(x,y,z,w);
	}
	
	public Vec4(Vec4 copy) {
		set(copy);
	}
	
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Vec4 orig) {
		set(orig.x, orig.y, orig.z, orig.w);
	}
	
	public void transform(Matrix4 mat) {
		set(mat.m00 * x + mat.m10 * y + mat.m20 * z + mat.m30 * w,
				mat.m01 * x + mat.m11 * y + mat.m21 * z + mat.m31 * w,
				mat.m02 * x + mat.m12 * y + mat.m22 * z + mat.m32 * w,
				mat.m03 * x + mat.m13 * y + mat.m23 * z + mat.m33 * w);
	}
}
