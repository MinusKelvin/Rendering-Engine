package minusk.render.math.geom;

import minusk.render.math.Matrix3;
import minusk.render.math.Vec2;

public interface Shape2D {
	public Vec2[] getVerticies();
	public float area();
	public void transform(Matrix3 mat);
}
