package minusk.render.util;

import minusk.render.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by MinusKelvin on 2015-08-09.
 */
public class LinearGradient {
	private ArrayList<Stop> stops = new ArrayList<>();
	private WrapMode wrapMode = WrapMode.CLAMP;
	
	public LinearGradient(Color color1, Color color2, Stop... stps) {
		stops.add(new Stop(color1,0));
		stops.add(new Stop(color2,1));
		addStops(stps);
	}
	
	public void addStop(Color color, float position) {
		addStops(new Stop(color, position));
	}
	
	public void addStops(Stop... stps) {
		Collections.addAll(stops, stps);
		stops.sort((Stop s1, Stop s2) -> (int) Math.signum(s1.position - s2.position));
	}
	
	public Color get(float position) {
		final float originalPosition = position;
		switch (wrapMode) {
			case CLAMP:
				position = Math.max(Math.min(position, 1), 0);
				break;
			case REPEAT:
				if (position != 0)
					position -= Math.ceil(position - 1);
				break;
			case MIRRORED_REPEAT:
				throw new UnsupportedOperationException("Not Yet Implemented");
				/*break;*/
		}
		assert position >= 0 && position <= 1 : "Erroneous condition: wrapMode="+wrapMode+" position="+originalPosition;
		
		Stop last = stops.get(0);
		for (int i = 1; i < stops.size(); last = stops.get(i++)) {
			if (last == null)
				continue;
			
			Stop current = stops.get(i);
			if (position <= current.position) {
				position -= last.position;
				position /= current.position - last.position;
				
				return new Color(
						current.color.getR()*position + last.color.getR()*(1-position),
						current.color.getG()*position + last.color.getG()*(1-position),
						current.color.getB()*position + last.color.getB()*(1-position),
						current.color.getA()*position + last.color.getA()*(1-position)
				);
			}
		}
		throw new AssertionError();
	}
	
	public void setWrapMode(WrapMode mode) {
		if (mode == null)
			throw new IllegalArgumentException("mode == null!");
		wrapMode = mode;
	}
	
	public WrapMode getWrapMode() {
		return wrapMode;
	}
	
	public static class Stop {
		private Color color;
		private float position;
		
		public Stop(Color color, float position) {
			if (color == null)
				throw new IllegalArgumentException("color == null!");
			if (position < 0 || position > 1)
				throw new IllegalArgumentException("position not in range 0 - 1 (inclusive, inclusive)");
			this.color = color;
			this.position = position;
		}
	}
	
	public enum WrapMode {
		CLAMP, REPEAT, MIRRORED_REPEAT
	}
}