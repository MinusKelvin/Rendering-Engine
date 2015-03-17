package minusk.render.math;

public class Matrix3 {
	public float m00,m10,m20,
				 m01,m11,m21,
				 m02,m12,m22;

	public Matrix3() {
		setIdentity();
	}

	public Matrix3(Matrix3 copyfrom) {
		m00 = copyfrom.m00;
		m01 = copyfrom.m01;
		m02 = copyfrom.m02;
		m10 = copyfrom.m10;
		m11 = copyfrom.m11;
		m12 = copyfrom.m12;
		m20 = copyfrom.m20;
		m21 = copyfrom.m21;
		m22 = copyfrom.m22;
	}

	public void setIdentity() {
		m00 = 1;
		m01 = 0;
		m02 = 0;
		m10 = 0;
		m11 = 1;
		m12 = 0;
		m20 = 0;
		m21 = 0;
		m22 = 1;
	}

	public void translate(float x, float y) {
		m20 += m00 * x + m10 * y;
		m21 += m01 * x + m11 * y;
	}
	
	public void rotate(float rads) {
		float c = (float) Math.cos(rads);
		float s = (float) Math.sin(rads);
		
		float t00 = m00 * c + m01 * -s;
		float t01 = m00 * s + m01 * c;
		float t10 = m10 * c + m11 * -s;
		float t11 = m10 * s + m11 * c;
		float t20 = m20 * c + m21 * -s;
		float t21 = m20 * s + m21 * c;
		
		m00 = t00;
		m10 = t10;
		m20 = t20;
		m01 = t01;
		m11 = t11;
		m21 = t21;
	}
	
	public static Matrix3 mul(Matrix3 left, Matrix3 right, Matrix3 dest) {
		if (dest == null)
			dest = new Matrix3();

		float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02;
		float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12;
		float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12;
		float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22;
		float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22;
		float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;

		return dest;
	}
}
