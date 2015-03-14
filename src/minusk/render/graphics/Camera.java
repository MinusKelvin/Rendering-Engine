package minusk.render.graphics;


import minusk.render.math.Matrix4f;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public abstract class Camera {
	public Matrix4f projection = new Matrix4f(), view = new Matrix4f(), combined = new Matrix4f();
	
	public float transX,transY,transZ;
	public float yaw,pitch,roll;
	public float zoom=1;
	public float near=0,far=1;
	
	public void update() {
		buildProjection();
		view.setIdentity();
		view.rotate(roll, 0,0,1);
		view.rotate(yaw, 0,1,0);
		view.rotate(pitch, (float)cos(yaw),0,(float)sin(yaw));
		view.translate(transX,transY,transZ);
		Matrix4f.mul(projection, view, combined);
	}
	
	public abstract void buildProjection();
}
