package minusk.render.core;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import minusk.render.math.Matrix4f;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

/**
 * General utilities, from distance calculations to creating direct <code>java.nio.ByteBuffer</code>s.
 * 
 * @author MinusKelvin
 */
public class Util {
	public static FloatBuffer toBuffer(float[] array) {
		return (FloatBuffer) ByteBuffer.allocateDirect(array.length*4)
				.order(ByteOrder.nativeOrder())
				.asFloatBuffer()
				.put(array)
				.position(0);
	}

	public static IntBuffer toBuffer(int[] array) {
		return (IntBuffer) ByteBuffer.allocateDirect(array.length*4)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer()
				.put(array)
				.position(0);
	}

	public static ByteBuffer directBuffer(int size) {
		return (ByteBuffer) ByteBuffer.allocateDirect(size*4)
				.order(ByteOrder.nativeOrder())
				.position(0);
	}

	public static ShortBuffer toBuffer(short[] array) {
		return (ShortBuffer) ByteBuffer.allocateDirect(array.length*2)
				.order(ByteOrder.nativeOrder())
				.asShortBuffer()
				.put(array)
				.position(0);
	}
	
	public static FloatBuffer toBuffer(Matrix4f mat) {
		float[] numbers = new float[16];
		numbers[0] = mat.m00;
		numbers[1] = mat.m01;
		numbers[2] = mat.m02;
		numbers[3] = mat.m03;
		numbers[4] = mat.m10;
		numbers[5] = mat.m11;
		numbers[6] = mat.m12;
		numbers[7] = mat.m13;
		numbers[8] = mat.m20;
		numbers[9] = mat.m21;
		numbers[10]= mat.m22;
		numbers[11]= mat.m23;
		numbers[12]= mat.m30;
		numbers[13]= mat.m31;
		numbers[14]= mat.m32;
		numbers[15]= mat.m33;
		return toBuffer(numbers);
	}

	public static FloatBuffer toBuffer(Matrix4f mat, FloatBuffer buf) {
		buf.put(mat.m00);
		buf.put(mat.m01);
		buf.put(mat.m02);
		buf.put(mat.m03);
		buf.put(mat.m10);
		buf.put(mat.m11);
		buf.put(mat.m12);
		buf.put(mat.m13);
		buf.put(mat.m20);
		buf.put(mat.m21);
		buf.put(mat.m22);
		buf.put(mat.m23);
		buf.put(mat.m30);
		buf.put(mat.m31);
		buf.put(mat.m32);
		buf.put(mat.m33);
		return buf;
	}

	public static int sum(int[] numbers) {
		int s = 0;
		for (int i = 0; i < numbers.length; i++)
			s += numbers[i];
		return s;
	}

	public static int sum(int[] numbers, int limit) {
		int s = 0;
		for (int i = 0; i < numbers.length && i < limit; i++)
			s += numbers[i];
		return s;
	}
	
	public static boolean all(boolean[] values) {
		for (int i = 0; i < values.length; i++)
			if (!values[i])
				return false;
		return true;
	}
	
	public static boolean any(boolean[] values) {
		for (int i = 0; i < values.length; i++)
			if (values[i])
				return true;
		return false;
	}
	
	public static boolean all(boolean[][] values) {
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++)
				if (!values[i][j])
					return false;
		return true;
	}
	
	public static boolean any(boolean[][] values) {
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++)
				if (values[i][j])
					return true;
		return false;
	}

	public static void copyTo(float[] from, float[] to, int indexOffset) {
		for (int i = 0; i < from.length; i++) {
			to[i+indexOffset] = from[i];
		}
	}

	public static boolean range(float number, float value, float range) {
		return number > value - range && number < value + range;
	}

	public static boolean checkBounds(int index, int max) {
		return index >= 0 && index < max;
	}

	public static boolean contains(int[] above, int i) {
		for (int j : above)
			if (j==i)
				return true;
		return false;
	}

	public static float clamp(float val, float min, float max) {
		return min(max(val,min),max);
	}
	
	public static float dist(float x1, float y1, float x2, float y2) {
		return (float) sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
}
