package minusk.render.graphics;


import minusk.render.math.Matrix4;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public abstract class Camera {
	public Matrix4 projection = new Matrix4(), view = new Matrix4(), combined = new Matrix4();
	
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
		Matrix4.mul(projection, view, combined);
	}
	
	public abstract void buildProjection();
}
