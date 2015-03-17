package minusk.render.math;

public class Easing {
	public static Vec2 linear(Vec2 p1, Vec2 p2, float t) {
		return new Vec2(p1.x*t + p2.x*(1-t), p1.y*t + p2.y*(1-t));
	}
	
	public static Vec2 bezier(Vec2[] points, float t) {
		if (points.length == 1)
			return points[0];
		Vec2[] pts = new Vec2[points.length-1];
		for (int i = 0; i < pts.length; i++)
			pts[i] = linear(points[i], points[i+1], t);
		return bezier(pts, t);
	}
}
