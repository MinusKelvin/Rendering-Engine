package minusk.render.math;


/**
 * Column Major Order<br>
 * <br>
 * m00  m10  m20  m30<br>
 * m01  m11  m21  m31<br>
 * m02  m12  m22  m32<br>
 * m03  m13  m23  m33<br>
 * 
 * @author MinusKelvin
 */
public class Matrix4 {
	public float m00,m10,m20,m30,
	             m01,m11,m21,m31,
	             m02,m12,m22,m32,
	             m03,m13,m23,m33;
	
	public Matrix4() {
		setIdentity();
	}
	
	public Matrix4(Matrix4 copyfrom) {
		m00 = copyfrom.m00;
		m01 = copyfrom.m01;
		m02 = copyfrom.m02;
		m03 = copyfrom.m03;
		m10 = copyfrom.m10;
		m11 = copyfrom.m11;
		m12 = copyfrom.m12;
		m13 = copyfrom.m13;
		m20 = copyfrom.m20;
		m21 = copyfrom.m21;
		m22 = copyfrom.m22;
		m23 = copyfrom.m23;
		m30 = copyfrom.m30;
		m31 = copyfrom.m31;
		m32 = copyfrom.m32;
		m33 = copyfrom.m33;
	}

	public void setIdentity() {
		m00 = 1;
		m01 = 0;
		m02 = 0;
		m03 = 0;
		m10 = 0;
		m11 = 1;
		m12 = 0;
		m13 = 0;
		m20 = 0;
		m21 = 0;
		m22 = 1;
		m23 = 0;
		m30 = 0;
		m31 = 0;
		m32 = 0;
		m33 = 1;
	}
	
	public void translate(float x, float y, float z) {
		m30 += m00 * x + m10 * y + m20 * z;
		m31 += m01 * x + m11 * y + m21 * z;
		m32 += m02 * x + m12 * y + m22 * z;
		m33 += m03 * x + m13 * y + m23 * z;
	}
	
	public void rotate(float rads, float axisx, float axisy, float axisz) {
		float c = (float) Math.cos(rads);
		float s = (float) Math.sin(rads);
		float oneminusc = 1.0f - c;
		float xy = axisx*axisy;
		float yz = axisy*axisz;
		float xz = axisx*axisz;
		float xs = axisx*s;
		float ys = axisy*s;
		float zs = axisz*s;

		float f00 = axisx*axisx*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		float f10 = xy*oneminusc-zs;
		float f11 = axisy*axisy*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axisz*axisz*oneminusc+c;

		float t00 = m00 * f00 + m10 * f01 + m20 * f02;
		float t01 = m01 * f00 + m11 * f01 + m21 * f02;
		float t02 = m02 * f00 + m12 * f01 + m22 * f02;
		float t03 = m03 * f00 + m13 * f01 + m23 * f02;
		float t10 = m00 * f10 + m10 * f11 + m20 * f12;
		float t11 = m01 * f10 + m11 * f11 + m21 * f12;
		float t12 = m02 * f10 + m12 * f11 + m22 * f12;
		float t13 = m03 * f10 + m13 * f11 + m23 * f12;
		m20 = m00 * f20 + m10 * f21 + m20 * f22;
		m21 = m01 * f20 + m11 * f21 + m21 * f22;
		m22 = m02 * f20 + m12 * f21 + m22 * f22;
		m23 = m03 * f20 + m13 * f21 + m23 * f22;
		m00 = t00;
		m01 = t01;
		m02 = t02;
		m03 = t03;
		m10 = t10;
		m11 = t11;
		m12 = t12;
		m13 = t13;
	}

	public static Matrix4 mul(Matrix4 left, Matrix4 right, Matrix4 dest) {
		if (dest == null)
			dest = new Matrix4();

		float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
		float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
		float m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
		float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
		float m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
		float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
		float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
		float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
		float m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
		float m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
		float m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
		float m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
		float m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}
	
	@Override
	public String toString() {
		return m00 + "\t" + m10 + "\t" + m20 + "\t" + m30 + "\t" + "\n" +
				m01 + "\t" + m11 + "\t" + m21 + "\t" + m31 + "\t" + "\n" +
				m02 + "\t" + m12 + "\t" + m22 + "\t" + m32 + "\t" + "\n" +
				m03 + "\t" + m13 + "\t" + m23 + "\t" + m33 + "\t" + "\n";
	}
}
