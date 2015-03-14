package minusk.render.graphics;


public class OrthoCamera extends Camera {
	public float left=-1,right=1,top=1,bottom=-1;
	public boolean ydown;

	public OrthoCamera(float left, float right, float top, float bottom) {
		this(left,right,top,bottom,-1,1);
	}

	public OrthoCamera(float left, float right, float top, float bottom, float near, float far) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.near = near;
		this.far = far;
		update();
	}

	public OrthoCamera() {}

	@Override
	public void buildProjection() {
		projection.setIdentity();
		
		if (near == far)
			throw new IllegalArgumentException("near must not be equal to far.");
		if (left == right)
			throw new IllegalArgumentException("left must not be equal to right.");
		if (bottom == top)
			throw new IllegalArgumentException("bottom must not be equal to top.");
		ydown = top < bottom;

		projection.m00 = 2/(right-left)*zoom;
		projection.m11 = 2/(top-bottom)*zoom;
		projection.m22 = -2/(far-near);
		projection.m30 = -(right+left)/(right-left)*zoom;
		projection.m31 = -(top+bottom)/(top-bottom)*zoom;
		projection.m32 = (near+far)/(far-near);
	}
}
